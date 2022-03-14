/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.UserOperations;

/**
 *
 * @author na170044d
 */
public class na170044_UserOperations implements UserOperations {
    private Connection connection=DB.getInstance().getConnection();
    @Override
    public boolean insertUser(String string, String string1, String string2, String string3) {
        if(string1.charAt(0)<'A' || string1.charAt(0)>'Z')
            return false;
        if(string2.charAt(0)<'A' || string2.charAt(0)>'Z')
            return false;
        if(string3.length()<8) 
            return false;
        int num=0;
        int let=0;
        for(int i=0; i<string3.length(); i++)
        {
            if(string3.charAt(i)>='0' && string3.charAt(i)<='9')
                num++;
            if((string3.charAt(i)>='a' && string3.charAt(i)<='z') || (string3.charAt(i)>='A' && string3.charAt(i)<='Z'))
                let++;
        }
        if(num==0 || let==0)
            return false;
        
        try(PreparedStatement ps=connection.prepareStatement("Select * from Korisnik where Username=?");){
            ps.setString(1, string);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next())
                    return false;
            } catch (SQLException ex) {
            Logger.getLogger(na170044_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Insert into Korisnik values (?,?,?,?)");)
        {
            ps.setString(1, string);
            ps.setString(2, string1);
            ps.setString(3, string2);
            ps.setString(4, string3);
            int val=ps.executeUpdate();
            if(val==0)
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public int declareAdmin(String string) {
        try(PreparedStatement ps=connection.prepareStatement("Select * from Korisnik where Username=?");){
            ps.setString(1, string);
            try(ResultSet rs=ps.executeQuery()){
                if(!rs.next())
                    return 2;
            } catch (SQLException ex) {
                Logger.getLogger(na170044_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Select * from Admin where Username=?");){
            ps.setString(1, string);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next())
                    return 1;
            } catch (SQLException ex) {
                Logger.getLogger(na170044_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Insert into Admin values (?)");){
            ps.setString(1, string);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(na170044_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public Integer getSentPackages(String... strings) {
        Integer ret=0;
        for(String name:strings)
        {
            try(PreparedStatement ps=connection.prepareStatement("Select * from Korisnik where Username=?");)
            {
                ps.setString(1,name);
                try(ResultSet rs=ps.executeQuery())
                {
                    if(!rs.next())
                        return null;
                } catch (SQLException ex) {
                    Logger.getLogger(na170044_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(na170044_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            try(PreparedStatement ps=connection.prepareStatement("Select count(IdPak) from Paket where Username=?");){
                ps.setString(1,name);
                try(ResultSet rs=ps.executeQuery()){
                    if(rs.next())
                    {
                        ret+=rs.getInt(1);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ret;
    }

    @Override
    public int deleteUsers(String... strings) {
        int ret=0;
        for(String name:strings)
        {
            try(PreparedStatement ps=connection.prepareStatement("Delete from Korisnik where Username=?");){
                ps.setString(1, name);
                ret+=ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ret;
    }

    @Override
    public List<String> getAllUsers() {
        List<String> list=new ArrayList<>();
        try(PreparedStatement ps=connection.prepareStatement("Select Username from Korisnik");){
            try(ResultSet rs=ps.executeQuery();){
                while(rs.next())
                    list.add(rs.getString(1));
            } catch (SQLException ex) {
                Logger.getLogger(na170044_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
}
