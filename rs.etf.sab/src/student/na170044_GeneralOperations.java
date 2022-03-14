/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.GeneralOperations;

/**
 *
 * @author na170044d
 */
public class na170044_GeneralOperations implements GeneralOperations {
    private Connection connection=DB.getInstance().getConnection();
    @Override
    public void eraseAll() {
        try(PreparedStatement ps=connection.prepareStatement("Delete from Ponuda")){
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(na170044_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Delete from ZahtevKurir")){
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(na170044_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Delete from Paket")){
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(na170044_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Delete from Voznja")){
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(na170044_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Delete from Kurir")){
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(na170044_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Delete from Admin")){
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(na170044_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Delete from Korisnik")){
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(na170044_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Delete from Vozilo")){
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(na170044_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Delete from Opstina")){
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(na170044_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps=connection.prepareStatement("Delete from Grad")){
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(na170044_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
