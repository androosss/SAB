/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import rs.etf.sab.operations.PackageOperations;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author na170044d
 */
public class na170044_PackageOperations implements PackageOperations {
    private Connection connection=DB.getInstance().getConnection();
    
    public static class MyPair<A,B> implements PackageOperations.Pair{
        A first;
        B second;
        
        public MyPair(A a,B b)
        {
            first=a;
            second=b;
        }
        
        
        @Override
        public A getFirstParam()
        {
            return first;
        }

        @Override
        public B getSecondParam()
        {
            return second;
        }
    }
    
    
    @Override
    public int insertPackage(int i, int i1, String string, int i2, BigDecimal bd) {
        try(PreparedStatement ps=connection.prepareStatement("Insert into Paket values (?,?,?,?,?,NULL,0,NULL,NULL,NULL)");)
        {
            ps.setInt(1, i);
            ps.setInt(2, i1);
            ps.setString(3,string);
            ps.setInt(4, i2);
            ps.setBigDecimal(5, bd);
            if(ps.executeUpdate()==0)
                return -1;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        int val=0;
        try(PreparedStatement ps=connection.prepareStatement("Select max(IdPak) from Paket");)
        {
            try(ResultSet rs=ps.executeQuery())
            {
                if(rs.next())
                    val=rs.getInt(1);
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return val;
    }

    @Override
    public int insertTransportOffer(String string, int i, BigDecimal bd) {
        try(PreparedStatement ps=connection.prepareStatement("Insert into Ponuda values (?,?,?)");)
        {
            ps.setString(1,string);
            ps.setInt(2, i);
            ps.setBigDecimal(3, bd);
            if(ps.executeUpdate()==0)
                return -1;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        int val=0;
        try(PreparedStatement ps=connection.prepareStatement("Select max(IdPon) from Ponuda");)
        {
            try(ResultSet rs=ps.executeQuery())
            {
                if(rs.next())
                    val=rs.getInt(1);
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return val;
    }

    @Override
    public boolean acceptAnOffer(int i) {
        String username="";
        int idpak=-1;
        BigDecimal proc=new BigDecimal(0);
        try(PreparedStatement ps=connection.prepareStatement("Select Username,IdPak,Procenat from Ponuda where IdPon=?")){
            ps.setInt(1,i);
            try(ResultSet rs=ps.executeQuery())
            {
                if(rs.next())
                {
                    username=rs.getString(1);
                    idpak=rs.getInt(2);
                    proc=rs.getBigDecimal(3);
                }
                else
                    return false;
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Update Paket set Kurir=? where IdPak=?"))
        {
            ps.setString(1, username);
            ps.setInt(2, idpak);
            if(ps.executeUpdate()==0)
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Update Paket set Status=1 where IdPak=?"))
        {
            ps.setInt(1, idpak);
            if(ps.executeUpdate()==0)
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Update Paket set Cena=(1+(?*0.01))*dbo.CenaJedneIsporuke(?) where IdPak=?"))
        {
            ps.setBigDecimal(1, proc);
            ps.setInt(2, idpak);
            ps.setInt(3, idpak);
            if(ps.executeUpdate()==0)
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Update Paket set DatumZahteva=? where IdPak=?"))
        {
            ps.setDate(1, new Date(System.currentTimeMillis()));
            ps.setInt(2, idpak);
            if(ps.executeUpdate()==0)
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public List<Integer> getAllOffers() {
        List<Integer> list=new ArrayList<>();
        try(PreparedStatement ps=connection.prepareStatement("Select IdPon from Ponuda");){
            try(ResultSet rs=ps.executeQuery())
            {
                while(rs.next())
                    list.add(rs.getInt(1));
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }  
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int i) {
        List<Pair<Integer, BigDecimal>> list=new ArrayList<>();
        try(PreparedStatement ps=connection.prepareStatement("Select IdPon,Procenat from Ponuda where IdPak=?");){
            ps.setInt(1,i);
            try(ResultSet rs=ps.executeQuery())
            {
                while(rs.next())
                {
                    Pair<Integer, BigDecimal> temp=new MyPair<>(rs.getInt(1),rs.getBigDecimal(2));
                    list.add(temp);
                }
                    
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }  
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }       
        return list;
    }
    
    @Override
    public boolean deletePackage(int i) {
        try(PreparedStatement ps=connection.prepareStatement("Delete from Paket where IdPak=?");)
        {
            ps.setInt(1, i);
            if(ps.executeUpdate()==0)
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public boolean changeWeight(int i, BigDecimal bd) {
        try(PreparedStatement ps=connection.prepareStatement("Update Paket set Tez=? where IdPak=?")){
            ps.setBigDecimal(1,bd);
            ps.setInt(2,i);
            if(ps.executeUpdate()==0)
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;  
    }

    @Override
    public boolean changeType(int i, int i1) {
        if(i1<0 || i1>2) return false;
        try(PreparedStatement ps=connection.prepareStatement("Update Paket set Tip=? where IdPak=?")){
            ps.setInt(1,i1);
            ps.setInt(2,i);
            if(ps.executeUpdate()==0)
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public Integer getDeliveryStatus(int i) {
        try(PreparedStatement ps=connection.prepareStatement("Select Status from Paket where IdPak=?");){
            ps.setInt(1, i);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next())
                    return rs.getInt(1);
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public BigDecimal getPriceOfDelivery(int i) {
        try(PreparedStatement ps=connection.prepareStatement("Select Cena from Paket where IdPak=? and Cena is not NULL");){
            ps.setInt(1, i);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next())
                    return rs.getBigDecimal(1);
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Date getAcceptanceTime(int i) {
        try(PreparedStatement ps=connection.prepareStatement("Select DatumZahteva from Paket where IdPak=? and DatumZahteva is not NULL");){
            ps.setInt(1, i);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next())
                    return rs.getDate(1);
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int i) {
        List<Integer> list=new ArrayList<>();
        try(PreparedStatement ps=connection.prepareStatement("Select IdPak from Paket where Tip=?")){
            ps.setInt(1, i);
            try(ResultSet rs=ps.executeQuery())
            {
                while(rs.next())
                    list.add(rs.getInt(1));
            }catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;  
    }

    @Override
    public List<Integer> getAllPackages() {
        List<Integer> list=new ArrayList<>();
        try(PreparedStatement ps=connection.prepareStatement("Select IdPak from Paket")){
            try(ResultSet rs=ps.executeQuery())
            {
                while(rs.next())
                    list.add(rs.getInt(1));
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;  
    }

    @Override
    public List<Integer> getDrive(String string) {
        int status=0;
        List<Integer> list=new ArrayList<>();
        try(PreparedStatement ps=connection.prepareStatement("Select Status from Kurir where Username=?");)
        {
            ps.setString(1,string);
            try(ResultSet rs=ps.executeQuery()){
                if(!rs.next())
                    return null;
                else
                    status=rs.getInt(1);
            }catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
                
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(status==0)
            return null;
        int idvoz=0;
        try(PreparedStatement ps=connection.prepareStatement("Select IdVoz from Voznja where Status=0 and Username=?");){
            ps.setString(1, string);
            try(ResultSet rs=ps.executeQuery();){
                if(!rs.next())
                    return null;
                else
                    idvoz=rs.getInt(1);
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Select IdPak from Paket where IdVoz=?");){
            ps.setInt(1,idvoz);
            try(ResultSet rs=ps.executeQuery();){
                while(rs.next())
                    list.add(rs.getInt(1));
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public int driveNextPackage(String string) {
        int status=0;
        List<Integer> list=new ArrayList<>();
        try(PreparedStatement ps=connection.prepareStatement("Select Status from Kurir where Username=?");)
        {
            ps.setString(1,string);
            try(ResultSet rs=ps.executeQuery()){
                if(!rs.next())
                    return -2;
                else
                    status=rs.getInt(1);
            }catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
                
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        int currentPackageId=0;
        int nextPackageId=-1;
        int idvoz=0;
        if(status==0) //Begin drive and set current package
        {
            try(PreparedStatement ps=connection.prepareStatement("Select IdPak from Paket where Status=1 and Kurir=?"))
            {
                ps.setString(1, string);
                try(ResultSet rs=ps.executeQuery();){
                    if(!rs.next())
                        return -1;
                } catch (SQLException ex) {
                    Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            try(PreparedStatement ps=connection.prepareStatement("Insert into Voznja values (0,0,?)");){
                ps.setString(1, string);
                if(ps.executeUpdate()==0)
                    return -2;
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            try(PreparedStatement ps=connection.prepareStatement("Select max(IdVoz) from Voznja");)
            {
                try(ResultSet rs=ps.executeQuery())
                {
                    if(!rs.next())
                        return -2;
                    else
                        idvoz=rs.getInt(1);
                } catch (SQLException ex) {
                    Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            try(PreparedStatement ps=connection.prepareStatement("Update Paket set IdVoz=? where Kurir=? and Status=1");){
                ps.setInt(1, idvoz);
                ps.setString(2,string);
                if(ps.executeUpdate()==0)
                    return -2;
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            try(PreparedStatement ps=connection.prepareStatement("Update Paket set Status=2 where IdVoz=?");){
                ps.setInt(1,idvoz);
                if(ps.executeUpdate()==0)
                    return -2;
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            try(PreparedStatement ps=connection.prepareStatement("Select IdPak from Paket where IdVoz=? and Status=2 order by DatumZahteva Asc");){
                ps.setInt(1, idvoz);
                try(ResultSet rs=ps.executeQuery();){
                    if(!rs.next())
                        return -2;
                    else
                        currentPackageId=rs.getInt(1);
                } catch (SQLException ex) {
                    Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            try(PreparedStatement ps=connection.prepareStatement("Update Kurir set Status=1 where Username=?");){
                ps.setString(1, string);
                if(ps.executeUpdate()==0)
                    return -2;
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            try(PreparedStatement ps=connection.prepareStatement("Select IdPak from Paket where IdVoz=? and Status=2 and IdPak!=? order by DatumZahteva Asc");){
                ps.setInt(1, idvoz);
                ps.setInt(2,currentPackageId);
                try(ResultSet rs=ps.executeQuery();){
                    if(rs.next())
                        nextPackageId=rs.getInt(1);
                } catch (SQLException ex) {
                    Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else // Set current package
        {
            try(PreparedStatement ps=connection.prepareStatement("Select IdVoz from Voznja where Username=? and Status=0");){
                ps.setString(1,string);
                try(ResultSet rs=ps.executeQuery();){
                    if(rs.next())
                        idvoz=rs.getInt(1);
                    else
                        return -2;
                } catch (SQLException ex) {
                    Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            try(PreparedStatement ps=connection.prepareStatement("Select IdPak from Paket where IdVoz=? and Status=2 order by DatumZahteva Asc");){
                ps.setInt(1, idvoz);
                try(ResultSet rs=ps.executeQuery()){
                    if(rs.next())
                        currentPackageId=rs.getInt(1);
                    else
                        return -2;
                } catch (SQLException ex) {
                   Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            try(PreparedStatement ps=connection.prepareStatement("Select IdPak from Paket where IdVoz=? and Status=2 and IdPak!=? order by DatumZahteva Asc");){
                ps.setInt(1, idvoz);
                ps.setInt(2,currentPackageId);
                try(ResultSet rs=ps.executeQuery();){
                    if(rs.next())
                        nextPackageId=rs.getInt(1);
                } catch (SQLException ex) {
                    Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        int x1=0,x2=0,y1=0,y2=0,tip=0,cena=0;
        BigDecimal pot=new BigDecimal(0),cenais=new BigDecimal(0);
        //Fetch data for Delivery
        try(PreparedStatement ps=connection.prepareStatement(
                "Select O1.KoordX,O1.KoordY,O2.KoordX,O2.KoordY,V.Tip,V.Potr,P.Cena\n"
              + "From Opstina O1,Opstina O2, Paket P, Kurir K, Vozilo V\n"
              + "Where O1.IdOps=P.IdOpsOd and O2.IdOps=P.IdOpsDo and P.Kurir=K.Username and K.Tab=V.Tab and P.IdPak=? "
        );){
            ps.setInt(1,currentPackageId);
            try(ResultSet rs=ps.executeQuery()){
                if(!rs.next())
                    return -2;
                else
                {
                    x1=rs.getInt(1);
                    y1=rs.getInt(2);
                    x2=rs.getInt(3);
                    y2=rs.getInt(4);
                    tip=rs.getInt(5);
                    pot=rs.getBigDecimal(6);
                    cenais=rs.getBigDecimal(7);
                }
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(tip==0)
            cena=15;
        if(tip==1)
            cena=32;
        if(tip==2)
            cena=36;
        double result=cenais.doubleValue()-cena*Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))*(pot.doubleValue());
        try(PreparedStatement ps=connection.prepareStatement("Update Paket set Status=3 where IdPak=?");){
            ps.setInt(1,currentPackageId);
            if(ps.executeUpdate()==0)
                return -2;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(nextPackageId==-1)
        {
            try(PreparedStatement ps=connection.prepareStatement("Update Voznja set Status=1 where IdVoz=?");){
                ps.setInt(1,idvoz);
                if(ps.executeUpdate()==0)
                    return -2;
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            try(PreparedStatement ps=connection.prepareStatement("Update Kurir set Status=0 where Username=?");){
                ps.setString(1,string);
                if(ps.executeUpdate()==0)
                    return -2;
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            double ukupno=0;
            try(PreparedStatement ps=connection.prepareStatement("Select Zarada from Voznja where IdVoz=?");){
                ps.setInt(1,idvoz);
                try(ResultSet rs=ps.executeQuery();){
                    if(rs.next())
                        ukupno=rs.getDouble(1);
                    else
                        return -2;
                } catch (SQLException ex) {
                    Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try(PreparedStatement ps=connection.prepareStatement("Update Kurir set Profit=Profit+? where Username=?");){
                ps.setDouble(1,ukupno+result);
                ps.setString(2,string);
                if(ps.executeUpdate()==0)
                    return -2;
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            try(PreparedStatement ps=connection.prepareStatement("Update Paket set Status=2 where IdPak=?");){
            ps.setInt(1,nextPackageId);
            if(ps.executeUpdate()==0)
                return -2;
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            int x3=0,y3=0;
            try(PreparedStatement ps=connection.prepareStatement("Select o.KoordX,o.KoordY from Paket p,Opstina o "
                    + "where p.idOpsOd=o.idOps and p.IdPak=?")){
                ps.setInt(1, nextPackageId);
                try(ResultSet rs=ps.executeQuery();){
                    if(rs.next())
                    {
                        x3=rs.getInt(1);
                        y3=rs.getInt(2);
                    }
                    else
                        return -2;
                } catch (SQLException ex) {
                    Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            result-=cena*Math.sqrt((x3-x2)*(x3-x2)+(y3-y2)*(y3-y2))*(pot.doubleValue());
        }
        try(PreparedStatement ps=connection.prepareStatement("Update Voznja set Zarada=Zarada+? where IdVoz=?");){
            ps.setDouble(1,result);
            ps.setInt(2,idvoz);
            if(ps.executeUpdate()==0)
                return -2;
        } catch (SQLException ex) {
            Logger.getLogger(na170044_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return currentPackageId;
    }
}
