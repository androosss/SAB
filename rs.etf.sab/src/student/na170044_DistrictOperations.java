/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.DistrictOperations;

/**
 *
 * @author na170044d
 */
public class na170044_DistrictOperations implements DistrictOperations {
    private Connection connection=DB.getInstance().getConnection();
    @Override
    public int insertDistrict(String string, int i, int i1, int i2) {
        try(PreparedStatement ps1=connection.prepareStatement("Insert into Opstina values (?,?,?,?)");){
            ps1.setString(2,string);
            ps1.setInt(1,i);
            ps1.setInt(3,i1);
            ps1.setInt(4,i2);
            if(ps1.executeUpdate()==0)
                return -1;
       
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        int max=0;
        try(PreparedStatement ps=connection.prepareStatement("Select max(IdOps) from Opstina");){
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next())
                    max=rs.getInt(1);
            } catch (SQLException ex) {
                Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return max;
    }

    @Override
    public int deleteDistricts(String... strings) {
        int ret=0;
        for(String name:strings)
        {
            try(PreparedStatement ps=connection.prepareStatement("Delete from Opstina where Naziv=?");){
                ps.setString(1, name);
                ret+=ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ret;    }

    @Override
    public boolean deleteDistrict(int i) {
        int val=0;
        try(PreparedStatement ps=connection.prepareStatement("Delete from Opstina where IdOps=?");){
            ps.setInt(1,i);
            val=ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(val==0) return false;
        else return true;    
    }

    @Override
    public int deleteAllDistrictsFromCity(String string) {
        int ret=0;
        try(PreparedStatement ps=connection.prepareStatement("Delete from Opstina where IdGra in (Select IdGra from Grad where Naziv=?)");){
            ps.setString(1, string);
            ret+=ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public List<Integer> getAllDistrictsFromCity(int i) {
        try(PreparedStatement ps=connection.prepareStatement("Select * from Grad where IdGra=?");){
            ps.setInt(1, i);
            try(ResultSet rs=ps.executeQuery())
            {
                if(!rs.next())
                    return null;
            }
            catch (SQLException ex) {
                Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        List<Integer> list=new ArrayList<>();
        try(PreparedStatement ps=connection.prepareStatement("Select IdOps from Opstina where IdGra=?");){
            ps.setInt(1, i);
            try(ResultSet rs=ps.executeQuery())
            {
                while(rs.next())
                    list.add(rs.getInt(1));
            }
            catch (SQLException ex) {
                Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public List<Integer> getAllDistricts() {
        List<Integer> list=new ArrayList<>();
        try(PreparedStatement ps=connection.prepareStatement("Select IdOps from Opstina");){
            try(ResultSet rs=ps.executeQuery())
            {
                while(rs.next())
                    list.add(rs.getInt(1));
            }
            catch (SQLException ex) {
                Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
  
}
