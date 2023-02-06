package Service;

//mport java.util.List;
import Model.Account;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.reset;

import java.util.ArrayList;
import java.util.List;

import DAO.*;

/*
 * Author: Mahlet Drar
 * class name: AccountService 
 * purpose: the purpose of AccountService class is to add new users with unique user name and valid password, 
 *  --and let accoun user log in with a valid username and password
 */

public class AccountService {

    private AccountDAO accountDAO;


    /**
     * no-args constructor for creating a new AccountService with a new AccountDAO,
     * this no-args constructor will be identified by the Controller service
     */
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    /**
     * Controller for AccountService when AccountDAO is provided
     * @param accountDAO
     */
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
      
    }


    /**
     * add new account users with username and password
     * the username must be unique and not null
     * the pasword length must be >= 4
     * the account_id will be auto generated in the account table
     * @return the persisted account
     */

     public Account addNewUser(Account account){
        if(account.getUsername().length() > 0 && isAccountUnique(account.getUsername())
                && accountDAO.getAccountByUserName(account.getUsername()) == null && account.getPassword().length() >= 4){
            Account newAct = accountDAO.insertNewAccountUser(account);
            return newAct;
        }
        else {
            return null;
        }

    }

    /*
     * a boolean to check if a given username is already existed 
     * @param username
     * @return true if a username is existed 
     */

    private boolean isAccountUnique(String username){
       if(accountDAO.getAccountByUserName(username) == null){
           return true;
       }
       return false;
    }

        
/*
 * account login with username and password
 * the provided username and password must match with the stored username and password in the database
 * the return should include the account_id 
 * @param account
 * @return account 
 */
    public Account login(Account account) {
        if(accountDAO.accountLogin(account) != null){
             Account logedAccount = accountDAO.accountLogin(account);
             return logedAccount;
        }
        else{
            return null;
        }
     
    }



}


