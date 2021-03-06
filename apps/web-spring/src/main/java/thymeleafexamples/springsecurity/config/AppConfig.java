package thymeleafexamples.springsecurity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import thymeleafexamples.springsecurity.PaymentDeserializer;
import thymeleafexamples.springsecurity.PaymentDeserializerStatus;
import thymeleafexamples.springsecurity.Utils;
import thymeleafexamples.springsecurity.report.ReportGenerator;
import thymeleafexamples.springsecurity.yandex.Payment;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@EnableScheduling
@ComponentScan(basePackages="thymeleafexamples.springsecurity")
@PropertySource("classpath:persistence-postgres.properties")
public class AppConfig implements WebMvcConfigurer {

	@Autowired
	private Environment env;

	@Autowired
	private ApplicationContext applicationContext;

	private Logger logger = Logger.getLogger(getClass().getName());

//	@Bean
//	public HttpSession session() {
//		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//		return attr.getRequest().getSession(true); // true == allow create
//	}

	@Bean
	@Scope(value="session")
	public VkApiClient vkApiClient() {
		return new VkApiClient(new HttpTransportClient());
	}



	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Payment.class, new PaymentDeserializer());
		mapper.registerModule(module);
		return mapper;
	}

	@Bean
	public ObjectMapper paymentDeserializersStatus() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Payment.class, new PaymentDeserializerStatus());
		mapper.registerModule(module);
		return mapper;
	}

	@Bean
	public CredentialsProvider yandexCredentialsProvider() {
		CredentialsProvider provider = new BasicCredentialsProvider();

		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(env.getProperty("shopId"), env.getProperty("pass"));
		provider.setCredentials(AuthScope.ANY, credentials);
		return provider;
	}

	@Bean
	public ReportGenerator reportGenerator() {
		ReportGenerator reportGenerator = new ReportGenerator();
		return reportGenerator;
	}
	
	@Bean
	public DataSource securityDataSource() {
		
		// create connection pool
		ComboPooledDataSource securityDataSource = new ComboPooledDataSource();

		// set the jdbc driver
		try {
			securityDataSource.setDriverClass(env.getRequiredProperty("jdbc.driverClassName"));
		}
		catch (PropertyVetoException exc) {
			throw new RuntimeException(exc);
		}
		
		// for sanity's sake, let's log url and user ... just to make sure we are reading the data
		logger.info("jdbc.url=" + env.getProperty("jdbc.url"));
		logger.info("jdbc.user=" + env.getProperty("jdbc.user"));
		
		// set database connection props
		securityDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
		securityDataSource.setUser(env.getProperty("jdbc.username"));
		securityDataSource.setPassword(env.getProperty("jdbc.password"));

		// set connection pool props
		securityDataSource.setInitialPoolSize(
		getIntProperty("connection.pool.initialPoolSize"));

		securityDataSource.setMinPoolSize(
				getIntProperty("connection.pool.minPoolSize"));
		
		securityDataSource.setMaxPoolSize(
				getIntProperty("connection.pool.maxPoolSize"));
		
		securityDataSource.setMaxIdleTime(
				getIntProperty( "connection.pool.maxIdleTime"));
				
		return securityDataSource;
	}
	
	private int getIntProperty(String propName) {
		
		String propVal = env.getProperty(propName);
		
		// now convert to int
		int intPropVal = Integer.parseInt(propVal);
		
		return intPropVal;
	}
	
	private Properties getHibernateProperties() {

		// set hibernate properties
		Properties props = new Properties();

		props.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		props.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		props.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
		props.setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
		props.setProperty("hibernate.connection.characterEncoding", env.getProperty("hibernate.connection.characterEncoding"));

		return props;				
	}

//	@Bean
//	@Scope(value = WebApplicationContext.SCOPE_SESSION,
//			proxyMode = ScopedProxyMode.TARGET_CLASS) //TODO WHAT IS PROXY MODE?
//	public SessionAttr sessionAttr() {
//		return new TodoList();
//	}




	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("utf-8");
		return messageSource;
	}

	@Bean(name="localeResolver")
	public LocaleResolver localeResolver(){
		SessionLocaleResolver resolver = new SessionLocaleResolver();
		resolver.setDefaultLocale(new Locale("ru", "RU"));
		return resolver;
	}

	@Bean
	public LocalValidatorFactoryBean getValidator() {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource());
		return bean;
	}

	@Bean
	public String yandexCreatePaymentTemplate() {
		return Utils.readLineByLineJava8("requests/generate_payment_template.json");
	}


//	@Bean
//	public void createSchemaWithHibernate5() {
//		Map map = new HashMap();
//		map.putAll(getHibernateProperties());
//		map.put("jdbc.url",env.getProperty("jdbc.url"));
//		map.put("jdbc.user",env.getProperty("jdbc.user"));
//		map.put("jdbc.password",env.getProperty("jdbc.password"));
//		StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder() //
//                .configure("persistence-postgres.properties")
//				.applySettings(map)
//				.build();
//
//		Metadata metadata = new MetadataSources(serviceRegistry) //
//				.buildMetadata();
//
//		new SchemaExport() //
//				.setOutputFile("db-schema.hibernate5.ddl") //
//				.create(EnumSet.of(TargetType.SCRIPT), metadata);
//
//		metadata.buildSessionFactory().close();
//		logger.log(Level.INFO,"createSchemaWithHibernate5");
//	}
	
	@Bean
	public LocalSessionFactoryBean sessionFactory(){
		
		// create session factorys
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		
		// set the properties
		sessionFactory.setDataSource(securityDataSource());
		sessionFactory.setPackagesToScan(env.getProperty("hibernate.packagesToScan"));
		sessionFactory.setHibernateProperties(getHibernateProperties());
		
		return sessionFactory;
	}
	
	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		
		// setup transaction manager based on session factory
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);

		return txManager;
	}
	

}
















