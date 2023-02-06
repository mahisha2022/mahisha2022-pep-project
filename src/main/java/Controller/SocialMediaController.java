package Controller;

import Model.*;
import Service.AccountService;
import Service.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import static org.mockito.Mockito.never;

import java.io.IOException;
import java.util.List;

/*
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */


public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */


    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */

    private AccountService accountService;
    private MessageService messageService;
    private ObjectMapper mapper;


    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
        this.mapper = new ObjectMapper();

    }

    public Javalin startAPI(){
        Javalin app = Javalin.create();
        app.post("/register", this::registerController);
        app.post("/login", this::loginController);
        app.post("messages", this::postMessages);
        app.get("/messages/{message_id}", this::messagesByIDController);
        app.get("/messages", this::getAllMessagesController);
        app.delete("messages/{message_id}", this::deleteMessageByIdController);
        app.patch("messages/{message_id}", this::updateMessageController);
        app.get("accounts/{account_id}/messages", this::getMessageByUserController);
         //app.start(8080);

        return app;
    }

    /*
     * Handler to register new user
     *If the new user/account registration returns a null account (meaning posting a new user/account was unsuccessful, the API will return a 400
            * message (client error)
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     * be available to this method automatically .
     *  @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     * */



    private void registerController(Context ctx) throws JsonProcessingException, IOException{

        Account account = mapper.readValue(ctx.body(), Account.class);
        Account register = accountService.addNewUser(account);
        if (register != null){
            ctx.json(mapper.writeValueAsString(register));
        }
        else {
            ctx.status(400);
        }
    }
    /*
     * handler for account login
     * if the login is not successful, the response status should be 401 (Unauthorized)
     */

    private void loginController(Context ctx) throws JsonProcessingException{
      
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account userLogin = accountService.login(account);
       

        if (userLogin != null) {
            ctx.json(mapper.writeValueAsString(userLogin));
        }

        else {
                ctx.status(401);
            }

    }
    /*
     * handler to process the creation of new messages 
     * if the creation of the message is not suceessfull, the response status should be 400 (client error)
     */

    private void postMessages(Context ctx) throws JsonProcessingException{

        Message message = mapper.readValue(ctx.body(), Message.class);

        Message newMessage = messageService.addMessages(message);

        if (newMessage != null){
            ctx.json(mapper.writeValueAsString(newMessage));
            ctx.status(200);
        }
        else {
            ctx.status(400);
        }
    }
/*
 * handler to retrieve a message by its ID
 * the response statu should always be 200, which is the default 
 */
    private void messagesByIDController(Context ctx) throws JsonProcessingException {

        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        ctx.json(messageService.getMessagesById(message_id));

    }

    private void getAllMessagesController(Context ctx){

        ctx.json(messageService.getAllMessages());

    }
    /*
     * handler to delete a message identified by a messge ID
     * if the message did not exist the response status should be 200, but the response body should be empty 
     */

    private  void deleteMessageByIdController(Context ctx) throws  JsonProcessingException {

        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deleted = messageService.deleteMessage(message_id);
        if(deleted != null){
            ctx.json(mapper.writeValueAsString(deleted));
        }
    }
    /*
     * handler to update a message text identified by a message ID
     * if the uodate is not successful, the response should be 400 (client error)
     */

    private void updateMessageController(Context ctx) throws JsonProcessingException{

        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updateMessage = messageService.updateMessage( message_id,  message);

        if (updateMessage != null){
            ctx.json(mapper.writeValueAsString(updateMessage));

        }
        else {
            ctx.status(400);
        }
    }
    /*
     * handler to retrieve all messages written by a parrticular user 
     * the response status should always be 200, which is the default 
     */

     private void getMessageByUserController(Context ctx) throws JsonProcessingException {

        int posted_by = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessageByAccountId(posted_by);
        ctx.json(mapper.writeValueAsString(messages));

    }


            



}

