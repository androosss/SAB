/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import rs.etf.sab.operations.VehicleOperations;
import java.sql.*;
import java.util.LinkedList;
/**
 *
 * @author na170044d
 */
public class na170044_VehicleOperations implements VehicleOperations{
    private Connection connection=DB.getInstance().getConnection();
    
    @Override
    public boolean insertVehicle(String string, int i, BigDecimal bd) {
        if(i<0 || i>2) return false;
        try(PreparedStatement ps=connection.prepareStatement("Select * from Vozilo where Tab=?");){
            ps.setString(1, string);
            try(ResultSet rs=ps.executeQuery();)
            {
                if(rs.next())
                    return false;
            } catch (SQLException ex) {
                Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Insert into Vozilo values (?,?,?)");){
            ps.setString(1, string);
            ps.setInt(2, i);
            ps.setBigDecimal(3, bd);
            if(ps.executeUpdate()==0)
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }

    @Override
    public int deleteVehicles(String... strings) {
        int ret=0;
        for(String name:strings)
        {
            try(PreparedStatement ps=connection.prepareStatement("Delete from Vozilo where Tab=?");){
                ps.setString(1, name);
                ret+=ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ret;
    }

    @Override
    public List<String> getAllVehichles() {
        List<String> ret=new LinkedList<>();
        try(PreparedStatement ps=connection.prepareStatement("Select Tab from Vozilo");){
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next())
                {
                    ret.add(rs.getString(1));
                }
            } catch (SQLException ex) {
                Logger.getLogger(na170044_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
    }

    @Override
    public boolean changeFuelType(String string, int i) {
        if(i<0 || i>2) return false;
        try(PreparedStatement ps=connection.prepareStatement("Update Vozilo set Tip=? where Tab=?");){
            ps.setInt(1,i);
            ps.setString(2, string);
            if(ps.executeUpdate()==0)
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public boolean changeConsumption(String string, BigDecimal bd) {
        try(PreparedStatement ps=connection.prepareStatement("Update Vozilo set Potr=? where Tab=?");){
            ps.setBigDecimal(1,bd);
            ps.setString(2, string);
            if(ps.executeUpdate()==0)
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
}
