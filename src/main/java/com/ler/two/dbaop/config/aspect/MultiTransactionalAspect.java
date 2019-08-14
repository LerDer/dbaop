package com.ler.two.dbaop.config.aspect;

import com.ler.two.dbaop.util.SpringBeanFactoryUtils;
import java.lang.reflect.Method;
import java.util.Stack;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

/**
 * @author lww
 * @date 2019-07-02 10:05 PM
 */
@Aspect
@Component
public class MultiTransactionalAspect {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Pointcut("@annotation(com.ler.two.dbaop.config.aspect.MultiTransactional)")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint point) {
		log.info("MultiTransactionalAspect_around_start_point:{}", point);
		Stack<DataSourceTransactionManager> dataSourceTransactionManagerStack = new Stack<>();
		Stack<TransactionStatus> transactionStatuStack = new Stack<>();
		Object ret = null;
		try {
			Method method = ((MethodSignature) point.getSignature()).getMethod();
			MultiTransactional annotation = method.getAnnotation(MultiTransactional.class);

			if (!openTransaction(dataSourceTransactionManagerStack, transactionStatuStack, annotation)) {
				return null;
			}
			ret = point.proceed();
			commit(dataSourceTransactionManagerStack, transactionStatuStack);
			return ret;
		} catch (Throwable e) {
			rollback(dataSourceTransactionManagerStack, transactionStatuStack);
			log.error(String.format("MultiTransactionalAspect, method:%s-%s occors error:", point.getTarget().getClass().getSimpleName(), point.getSignature().getName()), e);
			Assert.isTrue(false, e.getMessage());
		}
		log.info("MultiTransactionalAspect_around_end_ret:{}", ret);
		return ret;
	}

	private boolean openTransaction(Stack<DataSourceTransactionManager> dstms, Stack<TransactionStatus> tss, MultiTransactional mtt) {
		String[] transactionMangerNames = mtt.value();
		if (ArrayUtils.isEmpty(mtt.value())) {
			return false;
		}

		for (String beanName : transactionMangerNames) {
			DataSourceTransactionManager dstm = (DataSourceTransactionManager) SpringBeanFactoryUtils.getApplicationContext().getBean(beanName);
			TransactionStatus transactionStatus = dstm.getTransaction(new DefaultTransactionDefinition());
			dstms.push(dstm);
			tss.push(transactionStatus);
		}
		return true;
	}

	private void commit(Stack<DataSourceTransactionManager> dstms, Stack<TransactionStatus> tss) {
		while (!dstms.isEmpty()) {
			DataSourceTransactionManager transactionManager = dstms.pop();
			TransactionStatus transactionStatus = tss.pop();
			transactionManager.commit(transactionStatus);
		}
	}

	private void rollback(Stack<DataSourceTransactionManager> dstms, Stack<TransactionStatus> tss) {
		while (!dstms.isEmpty()) {
			DataSourceTransactionManager transactionManager = dstms.pop();
			TransactionStatus transactionStatus = tss.pop();
			transactionManager.rollback(transactionStatus);
		}
	}
}


