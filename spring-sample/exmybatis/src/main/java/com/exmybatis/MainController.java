package com.exmybatis;

import com.exmybatis.dao.MyDAO;
import com.exmybatis.domain.Echo;
import com.exmybatis.domain.User;
import com.exmybatis.request.UserParam;
import com.exmybatis.request.UserParamValidator;
import com.exmybatis.service.MyService;
import com.exmybatis.util.Paging;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;


@Controller
@PropertySource("classpath:some.properties")
public class MainController {
    protected Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private Environment environment;

    @Autowired
    private MyService myService;

    @Autowired
    private MyDAO myDAO;

    @Value("${hello.msg:default}")
    private String helloMsg;

    @Autowired
    UserParamValidator userParamValidator;

    @RequestMapping(value = "/*", method = RequestMethod.GET)
    public ResponseEntity<String> pageNotFound() {
        return new ResponseEntity<String>("Page not found", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value="/welcome", method = RequestMethod.GET)
    public String welcome(Locale locale) {
        logger.debug(locale);
        logger.debug(this.helloMsg);
        String helloMsg = environment.getProperty("hello.msg");
        String curMsg = messageSource.getMessage("greeting.msg", null, "default", locale);
        String engMsg = messageSource.getMessage("greeting.msg", null, Locale.US);
        return "welcome";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpSession session, ModelMap modelMap,
                        @Valid User user, BindingResult bindingResult, Locale locale) throws Exception {

        logger.debug(user);
        logger.debug(locale);

        if(bindingResult.hasErrors()){
            logger.debug("Binding Result has error!");
            List<ObjectError> errors = bindingResult.getAllErrors();
            for(ObjectError error : errors){
                logger.debug(error.getDefaultMessage());
            }

            return "login";
        }

        if (user.getPassword().equalsIgnoreCase("admin")) {
            modelMap.addAttribute("email", user.getEmail());
            session.setAttribute("email", user.getEmail());
            return "redirect:/welcome";
        }

        return "redirect:/pages/login_error.html";
    }

    @RequestMapping(value = "/getSuperAdmin", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
    public @ResponseBody String getSuperAdmin() throws SQLException {
        String superAdmin = myDAO.getSuperAdmin("SA");
        logger.info(superAdmin);
        return superAdmin;
    }

    @CrossOrigin
    @RequestMapping(value = "/postecho", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public ResponseEntity<Echo> postEcho(
            @RequestParam(value="text", required=false, defaultValue="") String text
    ) throws SQLException {
        return new ResponseEntity<Echo>(new Echo(text.toUpperCase()), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/postjsonecho", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public ResponseEntity<Echo> postJsonEcho(
            @RequestBody Echo echo
    ) throws SQLException {
        return new ResponseEntity<Echo>(new Echo(echo.getText().toUpperCase(), echo.getName().toUpperCase()), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/insertUser", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public ResponseEntity<Map> insertUser(
            @RequestBody User user
    ) throws SQLException {
        Map<String, String> map = new HashMap<>();
        map.put("result", String.valueOf(myDAO.insertUser(user)));
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @RequestMapping(value = "/adminUserNameList", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
    public @ResponseBody List<String> getAdminUserNameList() throws SQLException {
        List<String> adminUserNameList = myDAO.getAdminUserNameList();
        logger.info(adminUserNameList);
        return adminUserNameList;
    }

    @RequestMapping(value = "/allUserNameList", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
    public @ResponseBody List<String> getAllUserNameList() throws SQLException {
        List<String> allUserNameList = myDAO.getAllUserNameList();
        return allUserNameList;
    }


    @RequestMapping(value = "/allUserEmailList", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
    public @ResponseBody List<String> getAllUserEmailList() throws SQLException {
        List<String> allUserEmailList = myDAO.getAllUserEmailList();
        return allUserEmailList;
    }

    @RequestMapping(value = "/updateUserName", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
    public @ResponseBody List<String> updateUserName() throws SQLException {
        myDAO.updateAdminName();
        return myDAO.getAllUserNameList();
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public @ResponseBody List<String> find() throws SQLException {

        List<String> userEmailList = myDAO.findAdminEMail();
        logger.info(userEmailList);
        return userEmailList;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
    public @ResponseBody List<User> user() throws SQLException {
        List<User> allUsers = myDAO.getAllUser();
        return allUsers;
    }

    @RequestMapping(value = "/pagingUser", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
    public @ResponseBody List<User> pagingUser() throws SQLException {
        List<User> pagingUsers = myDAO.getPagingUserList(2, 3);
        logger.info(pagingUsers);
        return pagingUsers;
    }

    @RequestMapping(value = "/saveAdmin", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
    public @ResponseBody String sa() throws Exception {

        myService.saveAdmins();

        return "err";
    }

    @RequestMapping(value = "/exception", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
    public @ResponseBody String exception(
            @ModelAttribute UserParam userParam,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {

        if (bindingResult.hasErrors()) {
            logger.error("Parameter binding error.");
            StringBuilder errMsgBuilder = new StringBuilder();

            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error("RestError: " + error.getCode());
                logger.error("Message: " + error.getDefaultMessage());
                errMsgBuilder.append(error.getDefaultMessage() + "\n");
            }

            response.sendError(HttpServletResponse.SC_BAD_REQUEST, errMsgBuilder.toString());
        }

        userParamValidator.validate(userParam, bindingResult);

        if (bindingResult.hasErrors()) {
            logger.error("Parameter binding error.");
            StringBuilder errMsgBuilder = new StringBuilder();

            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error("RestError: " + error.getCode());
                logger.error("Message: " + error.getDefaultMessage());
                errMsgBuilder.append(error.getDefaultMessage() + "\n");
            }

            response.sendError(HttpServletResponse.SC_BAD_REQUEST, errMsgBuilder.toString());
        }

        throw new IllegalArgumentException("잘못된 요청입니다.");
    }

    @RequestMapping(value = "/ioexception", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
    public @ResponseBody String ioException(
            @ModelAttribute UserParam userParam,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        throw new IOException("io exception");
    }

    @RequestMapping(value = "/getAllUserCount", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
    public @ResponseBody String getAllUserCount() throws Exception {
        /// 전체 게시물
        /// 페이지당 보여질 게시물 수 : 5
        /// 전체 페이지수 : 전체 게시물 / 페이지당 보여질 게시물 수
        /// Pagination 숫자 : 10
        return Integer.toString(myDAO.getAllUserCount());
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam HashMap<String, String> paramMap) throws Exception {

        int totalCount = myDAO.getPagingUserListCount();
        String page = (String)paramMap.get("page");

        if (page == null || page.isEmpty()) {
            page = "1";
        }

        Paging paging = new Paging();
        paging.setPageNo(Integer.parseInt(page));
        paging.setPageSize(7);
        paging.setPagingBlockCount(20);
        paging.setTotalCount(totalCount);

        logger.info(paging.toString());
        int startOffset = (paging.getPageNo() - 1) * paging.getPageSize();
        List<User> users = myDAO.getPagingUserList(startOffset, paging.getPageSize());

        ModelAndView model = new ModelAndView("list");
        model.addObject("paging", paging);
        model.addObject("users", users);
        return model;
    }

    @RequestMapping(value = "/selectBool", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
    public @ResponseBody String foo(@RequestParam("value") boolean value) throws Exception {
        return Integer.toString(myDAO.selectBool(value));
    }

    @RequestMapping(value = "/selectUserList", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
    public @ResponseBody List<String> selectUserList(@RequestParam("value") boolean value) throws Exception {
        return myDAO.selectLists(value);
    }

    @RequestMapping(value = "/getNames", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
    public @ResponseBody List<String> getNames() throws Exception {

        ArrayList<String> names = new ArrayList<>(Arrays.asList("sa", "root", "sys"));
        List<String> result = myService.getNames(names);

        return result;
    }

    @RequestMapping(value = "/getUserNames", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
    public @ResponseBody List<String> getUserNames(
            @RequestParam(value="user_id", required=false, defaultValue="") String userId,
            @RequestParam(value="user_type", required=false, defaultValue="") String userType
    ) throws Exception {

        List<String> result = myService.getUserNames(userId, userType);

        return result;
    }

    @RequestMapping(value = "/getUserNamesCount", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
    public @ResponseBody int getUserNamesCount(
            @RequestParam(value="user_id", required=false, defaultValue="") String userId,
            @RequestParam(value="user_type", required=false, defaultValue="") String userType
    ) throws Exception {

        int count = myService.getUserNamesCount(userId, userType);

        return count;
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
    public @ResponseBody int updateUser(
            @RequestParam(value="user_id", required=false, defaultValue="") String userId,
            @RequestParam(value="user_name", required=false, defaultValue="") String userName,
            @RequestParam(value="user_type", required=false, defaultValue="") String userType
    ) throws Exception {

        int row = myService.updateUser(userId, userName, userType);

        return row;
    }
}

