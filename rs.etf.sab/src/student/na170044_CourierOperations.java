/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import rs.etf.sab.operations.CourierOperations;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author na170044d
 */
public class na170044_CourierOperations implements CourierOperations {
    private Connection connection=DB.getInstance().getConnection();

    @Override
    public boolean insertCourier(String string, String string1) {
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
        try(PreparedStatement ps=connection.prepareStatement("Insert into Kurir values (?,?,0,0,0)")){
            ps.setString(1, string);
            ps.setString(2,string1);
            int val=ps.executeUpdate();
            if(val==0) 
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }

    @Override
    public boolean deleteCourier(String string) {
        int val=0;
        try(PreparedStatement ps=connection.prepareStatement("Delete from Kurir where Username=?");){
            ps.setString(1,string);
            val=ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(val==0) return false;
        else return true;
    }

    @Override
    public List<String> getCouriersWithStatus(int i) {
        List<String> list=new ArrayList<>();
        try(PreparedStatement ps=connection.prepareStatement("Select Username from Kurir where Status=?")){
            ps.setInt(1,i);
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next())
                    list.add(rs.getString(1));
            } catch (SQLException ex) {
                Logger.getLogger(na170044_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;    
    }

    @Override
    public List<String> getAllCouriers() {
        List<String> list=new ArrayList<>();
        List<Integer> help=new ArrayList<>(); 
        try(PreparedStatement ps=connection.prepareStatement("Select Username,Profit from Kurir")){
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next())
                {
                    list.add(rs.getString(1));
                    help.add(rs.getInt(2));
                }
                for(int i=0; i<list.size(); i++)
                    for(int j=i+1; j<list.size(); j++)
                        if(Integer.compare(help.get(i),help.get(j))<0)
                        {
                            Integer a=help.get(i);
                            help.set(i, help.get(j));
                            help.set(j,a);
                            String b=list.get(i);
                            list.set(i, list.get(j));
                            list.set(j,b);
                        }
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public BigDecimal getAverageCourierProfit(int i) {
        double sum=0.0;
        int cnt=0;
        try(PreparedStatement ps=connection.prepareStatement("Select Profit from Kurir where BrPak>=?")){
            ps.setInt(1, i);
            try(ResultSet rs=ps.executeQuery();){
                while(rs.next())
                {
                    sum+=rs.getDouble(1);
                    cnt++;
                }
            } catch (SQLException ex) {
                Logger.getLogger(na170044_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(cnt>0) sum/=cnt;
        return new BigDecimal(sum);
    }
}
