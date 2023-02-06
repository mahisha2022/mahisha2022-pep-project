package Service;

import java.util.ArrayList;
import java.util.List;
import Model.*;
import DAO.*;


/*
 * Author: Mahlet Drar
 * class name: MessageService 
 * purpose: the purpose of MessageService class is to add new messages to existed users, retrieve, 
 * ---update and delete messages 
 *  
 */

public class MessageService {


    private MessageDAO messageDAO;
    private AccountDAO accountDAO;
    

    /*
     * no-args constructor for messageService which creates a MessageDAO.
     */
    public MessageService(){
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
        
    }

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO){
      this.messageDAO = messageDAO;
      this.accountDAO = accountDAO;
    }

    /*
     * the messageDAO used to persist a message to the database
     * Method should check if the message_id already exists before it  attempt to insert
     * @param message a message object
     * @return message if it was successfully inserted, null if not
     */

    public Message addMessages(Message message){
       
        if(isTextValid(message.getMessage_text()) && accountDAO.isValidAccount(message.getPosted_by())){
                Message newMessage = messageDAO.insertMessage(message);
                return newMessage;
            }
            else {
                return null;
            }
    }
    /**
     * use the messageDAO to retrieve all messages by message_id
     * @return all messages
     */
    public Message getMessagesById(int message_id) {
        return messageDAO.getMessageByID(message_id);
    }

    /**
     * Retrieve all messages
     * @return
     */

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }


    /*
     * delete message by its message_id
     * @param message_id
     * @return empty if no message is available by a given message id
     * return the deleted message, after a successful deletion 
     */
 
  
     public Message deleteMessage(int message_id){
             Message emptyMessage = new Message();
            if(messageDAO.getMessageByID(message_id) == emptyMessage){
                return emptyMessage;
            }
            else {
                Message deletedMessage = messageDAO.getMessageByID(message_id);
                messageDAO.deleteMessage(message_id);
                return deletedMessage;
            }
    
        }
    /*
     * update message by message_id only when the provided message_text length > 0 and < 255
     * @param message_id
     */

     public Message updateMessage(int message_id, Message message) {

        if (messageDAO.getMessageByID(message_id) != null && isTextValid(message.getMessage_text())) {
            messageDAO.updateMessage(message_id, message);
            return messageDAO.getMessageByID(message_id);
        } else {
            return null;
        }
    }

/*
 * @return list of message by account_id
 * if no message available by the given account_id, return null
 * @param account_id
 */
public List<Message> getMessageByAccountId(int posted_by){

    return messageDAO.getMessageUserID(posted_by);

}

/*
 * check if a given message_text is valid 
 * @return false if message text length >= 255 or empty 
 */
public boolean isTextValid(String message_text){
    if(message_text.length() >= 255 || message_text == ""){
        return false;
    }
    return true;
}


}
