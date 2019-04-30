package hello;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class HelloWorldHibernateXML {

    private SessionFactory sessionFactory;

    @Before
    public void init() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        try {
            Metadata metadata = new MetadataSources(registry).buildMetadata();
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
