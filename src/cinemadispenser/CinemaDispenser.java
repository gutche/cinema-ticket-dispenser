/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinemadispenser;

import java.io.FileNotFoundException;
import java.io.IOException;
/**
 *
 * @author gledrian
 */
public class CinemaDispenser {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException {
        Multiplex multiplex = new Multiplex();
        multiplex.start();
    }
    
}
