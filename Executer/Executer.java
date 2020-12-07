package Executer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

/***
 * Author: Constant Pagoui, Kelley Roberts, Alex Nelson
 * Project: Deliverable 2
 * Course: CS7843 Concept of Programming language
 * Lecturer: Pr. Jose Garrido
 * Program: Defines the class Executer
 */

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import Parser.Parser;
import Parser.Identifier;
import Parser.ParsingException;
import Parser.Program;
import Parser.SyntaxException;
import Scanner.LexicalException;
import Scanner.SCLScanner;

public class Executer {

    private static final String BorderLayout = null;

    public static Identifier accessIdentifier(Identifier identifier) throws ExecuterException {
        if(!SCLScanner.identifiers_table.containsKey(identifier.getId()))
            throw new ExecuterException("Variable "+identifier.getId()+" not declared");
        return SCLScanner.identifiers_table.get(identifier.getId());
    }

    public void Interpret(String filename) throws IOException, LexicalException, SyntaxException, ParsingException, ExecuterException {
        //clearListIdentifier();
        Parser parser = new Parser(filename);
        Program program = parser.parse();
        program.execute();
        filename = filename.substring(0, filename.indexOf("."))+"_parser_output.txt";
        File file = new File(filename);
		if(!file.exists())
			file.createNewFile();
		PrintWriter writer = new PrintWriter(file, "UTF-8");
        writer.println(program);
        writer.close();
        System.out.println("\nThe result of the syntax analysis of the program and\n"+
            "the parse tree have been successfully written in the file "+filename);

        System.out.println("kelements: "+SCLScanner.identifiers_table.get("kelements"));
        System.out.println("j: "+SCLScanner.identifiers_table.get("j"));
        System.out.println("sum_val: "+SCLScanner.identifiers_table.get("sum_val"));
    }

    public static void main(String[]args){
        
        JFrame frame = new JFrame();
        frame.setBounds(150, 150, 686, 462);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu mntest = new JMenu("Interpreter");
        menuBar.add(mntest);
        JMenuItem mi = new JMenuItem("Open...");
        mntest.add(mi);
        JLabel tarea = new JLabel(40, 100);
        JTextArea console = new JTextArea(10, 100);
        mi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                    BufferedReader input = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file)));
                    tarea.read(input, "READING FILE :-)");
                    } catch (Exception ex) {
                    ex.printStackTrace();
                    }
                } else {
                    System.out.println("Operation is CANCELLED :(");
                }}}
        );
        frame.getContentPane().add(tarea, "Center");
        frame.getContentPane().add(console, "South");
        frame.pack();
        frame.setVisible(true);
        
        Executer interpreter = new Executer();
        try {
            interpreter.Interpret("sum.scl");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LexicalException notRecognizeToken) {
            notRecognizeToken.printStackTrace();
        } catch (SyntaxException e) {
            e.printStackTrace();
        } catch (ParsingException e) {
            e.printStackTrace();
        }catch (ExecuterException e){
            e.printStackTrace();
        }
    }

}
