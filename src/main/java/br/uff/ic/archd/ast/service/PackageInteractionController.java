/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.ast.service;


import br.uff.ic.archd.javacode.JavaConstructorService;
import br.uff.ic.archd.javacode.JavaPackage;
import br.uff.ic.archd.javacode.JavaProject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author wallace
 */
public class PackageInteractionController implements ActionListener {

    private PackageInteractionView packageInteractionViewer;
    private JavaConstructorService javaConstructorService;
    private JavaProject javaProject;

    PackageInteractionController() {
        javaConstructorService = new JavaConstructorService();
        javaProject = javaConstructorService.createProjects("/home/wallace/mestrado/jEdit");
        String packagesString[] = new String[javaProject.getPackages().size()];
        for (int i = 0; i < javaProject.getPackages().size(); i++) {
            packagesString[i] = javaProject.getPackages().get(i).getName();
        }
        packageInteractionViewer = new PackageInteractionView(packagesString);
        packageInteractionViewer.setController(this);
        packageInteractionViewer.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(InteractionViewer.ACTION_UPDATE)) {
            showClassFunctions(packageInteractionViewer.getClassSelected());
        }
    }

    private void showClassFunctions(String packageName) {
        JavaPackage javaPackage = javaProject.getPackageByName(packageName);


        System.out.println("\n\n ********** Package: " + packageName);
        System.out.println("Packages call: ");
        for (JavaPackage javap : javaPackage.getPackagesCall()) {
            System.out.println("- " + javap.getName());
        }
        System.out.println("Packages that call this package: ");
        for (JavaPackage javap : javaProject.getPackagesThatCall(javaPackage)) {
            System.out.println("- " + javap.getName());
        }
        System.out.println("");
        
    }

    public static void main(String args[]) {
        PackageInteractionController packageInteractionController = new PackageInteractionController();

    }
}
