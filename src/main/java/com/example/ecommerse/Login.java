package com.example.ecommerse;

import java.sql.ResultSet;

public class Login {
    public Customer customerLogin(String userName,String password){
        String loginQuery="select * from customers where email='"+userName+"' and passsword='"+password+"'";
        DbConnection conn =new DbConnection();
        ResultSet rs=conn.getQueryTable(loginQuery);
        try {
            if(rs.next())
                return new Customer(rs.getInt("id"),rs.getString("name"),rs.getString("email"),rs.getString("mobile"));

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Login login =new Login();
        Customer customer =login.customerLogin("angad@gmail.com","Pass123");
        System.out.println("WELCOME:"+customer.getName());
    }

}
