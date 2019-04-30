package hello;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HelloWorldJPA {

    private EntityManagerFactory emf;

    @Before
    public void init() {
        emf = Persistence.createEntityManagerFactory("HelloWorldPU");
    }

    @After
    public void destory() {
        emf.close();
    }

    @Test
    public void persistence() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        // create
        Message message = new Message();
        message.setText("Hello World!");
        em.persist(message);

        // read
        List<Message> messages = em.createQuery("select m from Message m").getResultList();
        assertEquals(messages.size(), 1);
        assertEquals(messages.get(0).getText(), "Hello World!");

        // update
        messages.get(0).setText("Take me to your leader!");

        em.getTransaction().commit();
        em.close();
    }

}
