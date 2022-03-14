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
import rs.etf.sab.operations.CityOperations;

/**
 *
 * @author na170044d
 */
public class na170044_CityOperations implements CityOperations{
    private Connection connection=DB.getInstance().getConnection();
    @Override
    public boolean deleteCity(int idCity)
    {
        int val=0;
        try(PreparedStatement ps=connection.prepareStatement("Delete from Grad where IdGra=?");){
            ps.setInt(1,idCity);
            val=ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(val==0) return false;
        else return true;
    }
    @Override
    public int deleteCity(String... names)
    {
        int ret=0;
        for(String name:names)
        {
            try(PreparedStatement ps=connection.prepareStatement("Delete from Grad where Naziv=?");){
                ps.setString(1, name);
                ret+=ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ret;
    }
    @Override
    public List<Integer> getAllCities()
    {
        List<Integer> list=new ArrayList<>();
        try(PreparedStatement ps=connection.prepareStatement("Select IdGra from Grad");){
            try(ResultSet rs=ps.executeQuery();)
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
    public int insertCity​(String name, String postalCode)
    {
        try(PreparedStatement ps=connection.prepareStatement("Select * from Grad where PosBr=?");){
            ps.setString(1, postalCode);
            try(ResultSet rs=ps.executeQuery();)
            {
                if(rs.next())
                   return -1;
            }
            catch (SQLException ex) {
                Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Select * from Grad where Naziv=?");){
            ps.setString(1, name);
            try(ResultSet rs=ps.executeQuery();)
            {
                if(rs.next())
                   return -1;
            }
            catch (SQLException ex) {
                Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int ret=0;      
        try(PreparedStatement ps1=connection.prepareStatement("Insert into Grad values (?,?)");){
            ps1.setString(1,name);
            ps1.setString(2,postalCode);
            if(ps1.executeUpdate()==0)
                return -1;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps1=connection.prepareStatement("Select IdGra from Grad where PosBr=?");){
            ps1.setString(1,postalCode);
            try(ResultSet rs=ps1.executeQuery()){
                if(rs.next())
                    ret=rs.getInt(1);
            }
            catch (SQLException ex) {
                Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }
}
