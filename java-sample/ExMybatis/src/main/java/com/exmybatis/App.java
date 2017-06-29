package com.exmybatis;

import com.exmybatis.dao.MyDAO;
import com.exmybatis.domain.User;
import com.exmybatis.service.MyService;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;


public class App 
{
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        String[] springConfig =
                {
                        "classpath:applicationContext.xml",
                        "classpath:database/query.xml"
                };

        ClassPathXmlApplicationContext context = null;

        try {

            context = new ClassPathXmlApplicationContext(springConfig);

            SqlSession sql = context.getBean(SqlSession.class);

            MyDAO dao = context.getBean(MyDAO.class);
            System.out.println("SA : " + dao.getSuperAdmin("SA"));

            dao.printAllUserByResultHandler();

            List<String> nameList = dao.getAdminUserNameList();
            System.out.println(nameList);

            nameList = dao.getAllUserNameList();
            System.out.println(nameList);

            dao.updateAdminName();
            nameList = dao.getAllUserNameList();
            System.out.println(nameList);

            List<String> userEmailList = dao.findAdminEMail();
            System.out.println(userEmailList);

            List<User> allUsers = dao.getAllUser();
            List<User> pagingUsers = dao.getPagingUserList(2, 3);

            System.out.println(allUsers);
            System.out.println(pagingUsers);

            MyService myService = context.getBean(MyService.class);
            myService.saveAdmins();

        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            if (null != context) {
                context.close();
            }
        }
    }
}
