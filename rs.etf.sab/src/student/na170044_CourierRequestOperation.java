/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import rs.etf.sab.operations.CourierRequestOperation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author na170044d
 */
public class na170044_CourierRequestOperation implements CourierRequestOperation{
    private Connection connection=DB.getInstance().getConnection();

    @Override
    public boolean insertCourierRequest(String string, String string1) {
        try(PreparedStatement ps=connection.prepareStatement("Select * from Korisnik where Username=?")){
            ps.setString(1, string);
            try(ResultSet rs=ps.executeQuery()){
                if(!rs.next())
                    return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Select * from Kurir where Username=?")){
            ps.setString(1, string);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next())
                    return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Select * from Kurir where Tab=?")){
            ps.setString(1, string1);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next())
                    return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Select * from ZahtevKurir where Username=?")){
            ps.setString(1, string);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next())
                    return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Select * from ZahtevKurir where Tab=?")){
            ps.setString(1, string1);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next())
                    return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Insert into ZahtevKurir values (?,?)")){
            ps.setString(2, string);
            ps.setString(1,string1);
            int val=ps.executeUpdate();
            if(val==0) 
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }

    @Override
    public boolean deleteCourierRequest(String string) {
        try(PreparedStatement ps=connection.prepareStatement("Select * from ZahtevKurir where Username=?")){
            ps.setString(1, string);
            try(ResultSet rs=ps.executeQuery()){
                if(!rs.next())
                    return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Delete from ZahtevKurir where Username=?")){
            ps.setString(1, string);
            int val=ps.executeUpdate();
            if(val==0)
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public boolean changeVehicleInCourierRequest(String string, String string1) {
        try(PreparedStatement ps=connection.prepareStatement("Select * from ZahtevKurir where Username=?")){
            ps.setString(1, string);
            try(ResultSet rs=ps.executeQuery()){
                if(!rs.next())
                    return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Update ZahtevKurir set tab=? where Username=?")){
            ps.setString(1, string1);
            ps.setString(2, string);
            int val=ps.executeUpdate();
            if(val==0)
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public List<String> getAllCourierRequests() {
        List<String> list=new ArrayList<>();
        try(PreparedStatement ps=connection.prepareStatement("Select Username from ZahtevKurir"))
        {
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next())
                    list.add(rs.getString(1));
            }
            catch (SQLException ex) {
                Logger.getLogger(na170044_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean grantRequest(String string) {
        String tab=null;
        try(PreparedStatement ps=connection.prepareStatement("Select Tab from ZahtevKurir where Username=?");)
        {
            ps.setString(1,string);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next())
                {
                    tab=rs.getString(1);
                }
                else return false;
            } catch (SQLException ex) {
                Logger.getLogger(na170044_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Insert into Kurir values (?,?,0,0,0)")){
            ps.setString(1, string);
            ps.setString(2,tab);
            int val=ps.executeUpdate();
            if(val==0) 
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
}
