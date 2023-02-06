package DAO;

import Model.*;
import Util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Author: Mahlet Drar
 * class name: AccountDAO 
 * purpose: the purpose of AccountDAO class is to add new acount into account table in a database, and retrieve existed account 
 * ----by provided filtering values (username or user_id).
 */
 public class AccountDAO {

/*
 * insert new account into account table in a database
 * the account_id column is a primary key and auto incremented, it will be handeled by the database
 * @param account
 * @return account 
 */

    public Account insertNewAccountUser(Account account){

        Connection connection = ConnectionUtil.getConnection();
  
         try {
             
            String sql = "INSERT INTO account (username, password) VALUES (?,?)";
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             preparedStatement.setString(1, account.getUsername());
             preparedStatement.setString(2, account.getPassword());
  
             preparedStatement.executeUpdate();
  
             ResultSet pky = preparedStatement.getGeneratedKeys();
             while (pky.next()){
                 int generated_account_id = pky.getInt(1);
                 return new Account(generated_account_id, account.getUsername(), account.getPassword());
             }
          }
              catch (SQLException e){
              System.out.println(e.getMessage());
  
          }
  
          return null;
      }


      /*
       * retrieve account filterd by username
       * @param username
       * @return
       */
    public Account getAccountByUserName(String username){

        Connection connection = ConnectionUtil.getConnection();

        try {
            //Write SQL logic to retrieve all accounts
            String sql = "SELECT * FROM Account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return  account;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

 
/*
 * retrieve account by username and password. 
 * to retrieve the account both the username and password must match with the stored values in the account table for an account
 * use this method to aaccount loging method in the message service class
 * @param username
 * @param password
 * @return
 */
    public Account accountLogin(Account account){

        Connection connection = ConnectionUtil.getConnection();
        try {

            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            ResultSet rs = preparedStatement.executeQuery();


            while(rs.next()) {
                Account newAccount = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return  newAccount;


            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;

    }
    /*
     * retrieve account by ID
     * @param account_id
     * this method will be called to check if the account is valid 
     * 
     */

    public Account getAccountByID(int account_id){

        Connection connection = ConnectionUtil.getConnection();

        try {
            //Write SQL logic to retrieve all accounts
            String sql = "SELECT * FROM Account WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Account accountById = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return  accountById;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    /*
     * a boolean method to check if the account is valid 
     * @param account_id
     * return true if the account exist
     * this method will be use at the message service class to post message for a valid account
     */

    public boolean isValidAccount(int account_id){

        if(getAccountByID(account_id) == null ){
            return false;
        }
        return true;
    }




}
