package DAO;


import Model.*;
import Util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



/*
 * Author: Mahlet Drar
 * class name: MessageDAO 
 * purpose: the purpose of MessageDAO class is to add new messages to a message table in a database, retrieve messages, 
 * --update and delete messages in/from the message table 
 */
public class MessageDAO {

  
    public MessageDAO(){

    }

    /*
     * Insert new messages into a message table for existed account user 
     *
     * @param message
     */

        public Message insertMessage(Message message) {

        Connection connection = ConnectionUtil.getConnection();


        try {

            String sql = " INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?) ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());


            preparedStatement.executeUpdate();
            ResultSet pky = preparedStatement.getGeneratedKeys();
            while (pky.next()) {
                int generated_message_id = pky.getInt(1);
                return new Message(generated_message_id, message.getPosted_by(),
                        message.getMessage_text(), message.getTime_posted_epoch());
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // if insert statement is not successful, return null
        return null;
    }
/*
 * retrieve all messages from message table  
 * @return lists of messages 
 */

    public List<Message> getAllMessages() {

        List<Message> messages = new ArrayList<>();

        Connection connection = ConnectionUtil.getConnection();
        try {
            /**
             *  SQL statement to retrieve all messages
             */

            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                    messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /*
     * retrieve message from message table by message_id
     * @param message_id
     * return null if message is not existed 
     */

    public Message getMessageByID(int message_id) {

        Connection connection = ConnectionUtil.getConnection();

        try {
            /**
             *  SQL statement to retrieve all messages
             */

            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message newmessage = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                return newmessage;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
/*
 * @return a list of message by account_id
 * if no message is available by the given account_id, return null.
 */
public List<Message> getMessageUserID(int posted_by) {

    List<Message> messages = new ArrayList<>();

    Connection connection = ConnectionUtil.getConnection();

    try {
        /**
         * SQL statement to retrieve all messages filtered by message_id
         */

        String sql = "SELECT * FROM message WHERE posted_by = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, posted_by);

        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {

            Message newMessage = new Message(rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
            messages.add(newMessage);


        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());

    }
    return messages;

}


/*
 * delete message by message_id
 * if message_id doesn't exist, return null
 */

    public Message deleteMessage(int message_id){
        
        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }
    /*
     * update message text_text by message_id
     * @param message_id
     * @param message
     * @return an updated value of message
     */

    public Message updateMessage(int message_id, Message message){

        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = " UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2,message_id);

            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                Message updatedMessage = new  Message(rs.getInt("message_id"), 
                                            rs.getInt("posted_by"),
                                            rs.getString("message_text"), 
                                            rs.getLong("time_posted_epoch")) ;
                return updatedMessage;
       

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return  null;
    }




}


