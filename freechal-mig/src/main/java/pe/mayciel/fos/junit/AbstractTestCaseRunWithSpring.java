package pe.mayciel.fos.junit;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * spring 환경 로딩 상태에서 junit test 를 할 수 있도록 지원 해 주는 클래스.
 * @author Hwang Seong-wook
 * @since 2013. 01. 04.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractTestCaseRunWithSpring {
}