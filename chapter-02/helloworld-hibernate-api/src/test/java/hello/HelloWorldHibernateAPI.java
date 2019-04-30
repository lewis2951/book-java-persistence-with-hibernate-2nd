package hello;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class HelloWorldHibernateAPI {

    private SessionFactory sessionFactory;

    @Before
    public void init() {
        StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
        serviceRegistryBuilder
                .applySetting("hibernate.connection.driver_class", "org.h2.Driver")
                .applySetting("hibernate.connection.url", "jdbc:h2:mem:jpa")
                .applySetting("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        serviceRegistryBuilder
                .applySetting("hibernate.format_sql", "true")
                .applySetting("hibernate.use_sql_comments", "true")
                .applySetting("hibernate.hbm2ddl.auto", "create-drop");
        ServiceRegistry registry = serviceRegistryBuilder.build();
        try {
            MetadataSources metadataSources = new MetadataSources(registry);
            metadataSources.addAnnotatedClass(
                    hello.Message.class
            );
            MetadataBuilder metadataBuilder = metadataSources.getMetadataBuilder();
            Metadata metadata = metadataBuilder.build();
            assertEquals(metadata.getEntityBindings().size(), 1);

            sessionFactory = metadata.buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @After
    public void destory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void persistence() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        // create
        Message message = new Message();
        message.setText("Hello World!");
        session.persist(message);

        // read
        List<Message> messages = session.createQuery("from Message").list();
        assertEquals(messages.size(), 1);
        assertEquals(messages.get(0).getText(), "Hello World!");

        // update
        messages.get(0).setText("Take me to your leader!");

        session.getTransaction().commit();
        session.close();
    }

}
