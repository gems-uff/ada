/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.service.mining;

import br.uff.ic.archd.javacode.JavaAbstract;
import br.uff.ic.archd.javacode.JavaClass;
import br.uff.ic.archd.javacode.JavaConstructorService;
import br.uff.ic.archd.javacode.JavaMethod;
import br.uff.ic.archd.javacode.JavaMethodInvocation;
import br.uff.ic.archd.javacode.JavaPackage;
import br.uff.ic.archd.javacode.JavaProject;
import br.uff.ic.archd.model.Project;
import br.uff.ic.dyevc.application.branchhistory.model.ProjectRevisions;
import br.uff.ic.dyevc.application.branchhistory.model.Revision;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class TransformFromTuple {

    private List<String> getMethodsChanges(JavaMethod antMethod, JavaMethod jm, JavaClass antClass) {
        //TO DO ******** aqui também ver caso tenha mudado a assinatura
        List<String> list = new LinkedList();
        if (antMethod != null && jm != null) {
            if (jm.getNumberOfLines() != antMethod.getNumberOfLines()) {
                if (jm.getNumberOfLines() > antMethod.getNumberOfLines()) {
                    list.add("number_of_lines:+ ");
                } else {
                    list.add("number_of_lines:- ");
                }
            }
            if (jm.getSizeInChars() != antMethod.getSizeInChars()) {
                if (jm.getSizeInChars() > antMethod.getSizeInChars()) {
                    list.add("size_in_chars:+ ");
                } else {
                    list.add("size_in_chars:- ");
                }
            }
            if (jm.getNumberOfLocalVariables() != antMethod.getNumberOfLocalVariables()) {
                if (jm.getNumberOfLocalVariables() > antMethod.getNumberOfLocalVariables()) {
                    list.add("number_of_local_variables:+ ");
                } else {
                    list.add("number_of_local_variables:- ");
                }
            }
            if (jm.getCyclomaticComplexity() != antMethod.getCyclomaticComplexity()) {
                if (jm.getCyclomaticComplexity() > antMethod.getCyclomaticComplexity()) {
                    list.add("cyclomatic_complexity:+ ");
                } else {
                    list.add("cyclomatic_complexity:- ");
                }
            }
            if (jm.getMethodInvocations().size() != antMethod.getMethodInvocations().size()) {
                if (jm.getMethodInvocations().size() > antMethod.getMethodInvocations().size()) {
                    list.add("number_of_external_method_invocations:+ ");
                } else {
                    list.add("number_of_external_method_invocations:- ");
                }
            }
            if (jm.getAccessToForeignDataNumber() != antMethod.getAccessToForeignDataNumber()) {
                if (jm.getAccessToForeignDataNumber() > antMethod.getAccessToForeignDataNumber()) {
                    list.add("access_to_foreign_data_number:+ ");
                } else {
                    list.add("access_to_foreign_data_number:- ");
                }
            }
            if (jm.getAccessToLocalDataNumber() != antMethod.getAccessToLocalDataNumber()) {
                if (jm.getAccessToLocalDataNumber() > antMethod.getAccessToLocalDataNumber()) {
                    list.add("access_to_local_data_number:+ ");
                } else {
                    list.add("access_to_local_data_number:- ");
                }
            }
            if (jm.getChangingClassesMetric() != antMethod.getChangingClassesMetric()) {
                if (jm.getChangingClassesMetric() > antMethod.getChangingClassesMetric()) {
                    list.add("change_classes_metric:+ ");
                } else {
                    list.add("change_classes_metric:- ");
                }
            }
            if (jm.getChangingMethods().size() != antMethod.getChangingMethods().size()) {
                if (jm.getChangingMethods().size() > antMethod.getChangingMethods().size()) {
                    list.add("number_of_changing_methods:+ ");
                } else {
                    list.add("number_of_changing_methods:- ");
                }
            }
            if (jm.getChangingMethodsMetric() != antMethod.getChangingMethodsMetric()) {
                if (jm.getChangingMethodsMetric() > antMethod.getChangingMethodsMetric()) {
                    list.add("changing_methods_metric:+ ");
                } else {
                    list.add("changing_methods_metric:- ");
                }
            }
            if (jm.getForeignDataProviderNumber() != antMethod.getForeignDataProviderNumber()) {
                if (jm.getForeignDataProviderNumber() > antMethod.getForeignDataProviderNumber()) {
                    list.add("foreign_data_provider_number:+ ");
                } else {
                    list.add("foreign_data_provider_number:- ");
                }
            }
            if (jm.getInternalMethodInvocations().size() != antMethod.getInternalMethodInvocations().size()) {
                if (jm.getInternalMethodInvocations().size() > antMethod.getInternalMethodInvocations().size()) {
                    list.add("number_of_internal_method_invocations:+ ");
                } else {
                    list.add("number_of_internal_method_invocations:- ");
                }
            }
            if (jm.getJavaExternalAttributeAccessList().size() != antMethod.getJavaExternalAttributeAccessList().size()) {
                if (jm.getJavaExternalAttributeAccessList().size() > antMethod.getJavaExternalAttributeAccessList().size()) {
                    list.add("number_of_external_access_attribute:+ ");
                } else {
                    list.add("number_of_external_access_attribute:- ");
                }
            }
            if (!jm.getReturnType().getFullQualifiedName().equals(antMethod.getReturnType().getFullQualifiedName())) {
                list.add("change_return_type:+ ");
            }

            if (jm.isAbstract() != antMethod.isAbstract()) {
                if (jm.isAbstract()) {
                    list.add("change_to_abstract:+ ");
                } else {
                    list.add("change_to_not_abstract:+ ");
                }
            }
            if (jm.isPublic() != antMethod.isPublic()) {
                if (jm.isPublic()) {
                    list.add("change_to_public:+ ");
                } else {
                    if (jm.isPrivate()) {
                        list.add("change_to_private:+ ");
                    } else {
                        list.add("change_to_protected:+ ");
                    }
                }
            }
            if (jm.isPrivate() != antMethod.isPrivate()) {
                if (jm.isPrivate()) {
                    list.add("change_to_private:+ ");
                } else {
                    if (jm.isPublic()) {
                        list.add("change_to_public:+ ");
                    } else {
                        list.add("change_to_protected:+ ");
                    }
                }
            }

            if (jm.isStatic() != antMethod.isStatic()) {
                if (jm.isStatic()) {
                    list.add("change_to_static:+ ");
                } else {
                    list.add("change_to_not_static:+ ");
                }
            }

            if (jm.isFinal() != antMethod.isFinal()) {
                if (jm.isFinal()) {
                    list.add("change_to_final:+ ");
                } else {
                    list.add("change_to_not_final:+ ");
                }
            }

            if (jm.isSynchronized() != antMethod.isSynchronized()) {
                if (jm.isSynchronized()) {
                    list.add("change_to_synchronized:+ ");
                } else {
                    list.add("change_to_not_synchronized:+ ");
                }
            }

            if (jm.isAnAcessorMethod() != antMethod.isAnAcessorMethod()) {
                if (jm.isAnAcessorMethod()) {
                    list.add("change_to_accessor_method:+ ");
                } else {
                    list.add("change_to_not_accessor_method:+ ");
                }
            }

            /*if (!jm.isAnAcessorMethod() && antMethod.isAnAcessorMethod()) {
             if (!jm.getAccessedAttribute().equals(antMethod.getAccessedAttribute())) {
             tupleStr = tupleStr + "change_to_accessor_method:+ ";
             } 
             }*/

            if (jm.isChangeInternalState() != antMethod.isChangeInternalState()) {
                if (jm.isChangeInternalState()) {
                    list.add("change_to_change_internal_state:+ ");
                } else {
                    list.add("change_to_not_change_internal_state:+ ");
                }
            }

            if (jm.isChangeInternalStateByMethodInvocations() != antMethod.isChangeInternalStateByMethodInvocations()) {
                if (jm.isChangeInternalState()) {
                    list.add("change_to_change_internal_state_by_method_invocation:+ ");
                } else {
                    list.add("change_to_not_change_internal_state_by_method_invocation:+ ");
                }
            }
        }

        return list;
    }

    private List<String> getClassesChanges(JavaClass antClass, JavaClass jc) {
        List<String> list = new LinkedList();

        if (jc.getAttributes().size() != antClass.getAttributes().size()) {
            if (jc.getAttributes().size() > antClass.getAttributes().size()) {
                list.add("number_of_attributes:+ ");
            } else {
                list.add("number_of_attributes:- ");
            }
        }
        if (jc.getMethods().size() != antClass.getMethods().size()) {
            if (jc.getMethods().size() > antClass.getMethods().size()) {
                list.add("number_of_methods:+ ");
            } else {
                list.add("number_of_methods:- ");
            }
        }
        if (jc.getTotalCyclomaticComplexity() != antClass.getTotalCyclomaticComplexity()) {
            if (jc.getTotalCyclomaticComplexity() > antClass.getTotalCyclomaticComplexity()) {
                list.add("total_cyclomatic_complexity:+ ");
            } else {
                list.add("total_cyclomatic_complexity:- ");
            }
        }
        if (jc.getImplementedInterfaces().size() != antClass.getImplementedInterfaces().size()) {
            if (jc.getImplementedInterfaces().size() > antClass.getImplementedInterfaces().size()) {
                list.add("number_of_implemented_interfaces:+ ");
            } else {
                list.add("number_of_implemented_interfaces:- ");
            }
        }
        if (jc.getAccessToForeignDataNumber() != antClass.getAccessToForeignDataNumber()) {
            if (jc.getAccessToForeignDataNumber() > antClass.getAccessToForeignDataNumber()) {
                list.add("access_to_foreign_data_number_class:+ ");
            } else {
                list.add("access_to_foreign_data_number_class:- ");
            }
        }
        if (jc.getClientClasses().size() != antClass.getClientClasses().size()) {
            if (jc.getClientClasses().size() > antClass.getClientClasses().size()) {
                list.add("number_of_client_classes_class:+ ");
            } else {
                list.add("number_of_client_classes_class:- ");
            }
        }
        if (jc.getClientPackages().size() != antClass.getClientPackages().size()) {
            if (jc.getClientPackages().size() > antClass.getClientPackages().size()) {
                list.add("number_of_client_packages_class:+ ");
            } else {
                list.add("number_of_client_packages_class:- ");
            }
        }
        if (jc.getExternalDependencyClasses().size() != antClass.getExternalDependencyClasses().size()) {
            if (jc.getExternalDependencyClasses().size() > antClass.getExternalDependencyClasses().size()) {
                list.add("number_of_external_dependency_classes_class:+ ");
            } else {
                list.add("number_of_external_dependency_classes_class:- ");
            }
        }
        if (jc.getExternalDependencyPackages().size() != antClass.getExternalDependencyPackages().size()) {
            if (jc.getExternalDependencyPackages().size() > antClass.getExternalDependencyPackages().size()) {
                list.add("number_of_external_dependency_packages_class:+ ");
            } else {
                list.add("number_of_external_dependency_packages_class:- ");
            }
        }
        if (jc.getInternalDependencyClasses().size() != antClass.getInternalDependencyClasses().size()) {
            if (jc.getInternalDependencyClasses().size() > antClass.getInternalDependencyClasses().size()) {
                list.add("number_of_internal_dependency_classes_class:+ ");
            } else {
                list.add("number_of_internal_dependency_classes_class:- ");
            }
        }
        if (jc.getNumberOfDirectConnections() != antClass.getNumberOfDirectConnections()) {
            if (jc.getNumberOfDirectConnections() > antClass.getNumberOfDirectConnections()) {
                list.add("number_of_direct_connections:+ ");
            } else {
                list.add("number_of_direct_connections:- ");
            }
        }
        if (jc.getintraPackageDependentClass().size() != antClass.getintraPackageDependentClass().size()) {
            if (jc.getintraPackageDependentClass().size() > antClass.getintraPackageDependentClass().size()) {
                list.add("number_of_intra_package_dependent_class:+ ");
            } else {
                list.add("number_of_intra_package_dependent_class:- ");
            }
        }
        return list;
    }
    
    public void crateStatistics(ProjectRevisions newProjectRevisions, Project project, JavaConstructorService javaConstructorService){
        
    }

    public List<String> transfFromMethodsToTuples(ProjectRevisions newProjectRevisions, Project project, JavaConstructorService javaConstructorService) {
        List<Sequence> sequences = new LinkedList();

        HashMap<String, Sequence> hashMap = new HashMap();


        System.out.println("Criando sequencias de métodos");
        Revision rev = newProjectRevisions.getRoot();
        Revision antRevision = null;
        JavaProject ant = null;
        int k = 0;
        TemporaryFileManager temporaryFileManager = new TemporaryFileManager();
        while (rev != null) {
            //JavaProject jp = javaProjects.get(i);
            JavaProject jp = null;
            //System.out.println("REV ID: "+rev.getId());
            System.out.println("********************************* vai pegar um projeto completo");
            jp = javaConstructorService.getProjectByRevisionAndSetRevision(project.getName(), project.getCodeDirs(), project.getPath(), rev.getId(), newProjectRevisions.getName());
            System.out.println("********************************* já pegou um projeto completo");


            //System.gc();

            k++;

            if (ant != null) {
                System.out.println("Sequencia: " + k);
                List<JavaClass> godClassEmerge = new LinkedList();
                List<JavaClass> godClassCorrect = new LinkedList();
                getGodClassesChange(ant, jp, godClassEmerge, godClassCorrect);
                List<JavaPackage> godPackageEmerge = new LinkedList();
                List<JavaPackage> godPackageCorrect = new LinkedList();
                getGodPackageChange(ant, jp, godPackageEmerge, godPackageCorrect);
                List<JavaClass> misplacedClassEmerge = new LinkedList();
                List<JavaClass> misplacedClassCorrect = new LinkedList();
                getMisplacedClassChange(ant, jp, misplacedClassEmerge, misplacedClassCorrect);

                for (JavaAbstract javaAbstract : jp.getClasses()) {
                    JavaClass jc = (JavaClass) javaAbstract;
                    JavaClass antClass = null;

                    JavaAbstract antAbstract = ant.getClassByName(jc.getFullQualifiedName());
                    if (antAbstract != null) {
                        if (antAbstract.getClass() == JavaClass.class) {
                            antClass = (JavaClass) ant.getClassByName(jc.getFullQualifiedName());
                        }
                    }

                    if (antClass != null) {
                        List<JavaMethod> featureEnvyEmerge = new LinkedList();
                        List<JavaMethod> featureEnvyCorrect = new LinkedList();
                        getFeatureEnvyChange(antClass, jc, featureEnvyEmerge, featureEnvyCorrect);
                        List<JavaMethod> shotgunEmerge = new LinkedList();
                        List<JavaMethod> shotgunCorrect = new LinkedList();
                        getShotgunSurgeryChange(antClass, jc, shotgunEmerge, shotgunCorrect);
                        List<JavaMethod> godMethodEmerge = new LinkedList();
                        List<JavaMethod> godMethodCorrect = new LinkedList();
                        getGodMethodChange(antClass, jc, godMethodEmerge, godMethodCorrect);

                        for (JavaMethod jm : jc.getMethods()) {
                            
                            
                            
                            JavaMethod antMethod = antClass.getMethodBySignature(jm.getMethodSignature());
                            //TO DO ******** aqui também ver caso tenha mudado a assinatura
                            if (antMethod != null) {
                                //aqui começa a criação das sequencias
                                //Sequence sequence = hashMap.get(jc.getFullQualifiedName() + ":" + jm.getMethodSignature());
                                
                                //if (sequence == null) {
                               //     sequence = new Sequence();
                                //    hashMap.put(jc.getFullQualifiedName() + ":" + jm.getMethodSignature(), sequence);
                               // }
                                //String tupleStr = "";
                                StringBuilder tupleStrBuilder = new StringBuilder("");
                                if (jm.getNumberOfLines() != antMethod.getNumberOfLines()) {
                                    if (jm.getNumberOfLines() > antMethod.getNumberOfLines()) {
                                        tupleStrBuilder.append("number_of_lines:+ ");
                                        //tupleStr = tupleStr + "number_of_lines:+ ";
                                    } else {
                                        tupleStrBuilder.append("number_of_lines:- ");
                                        //tupleStr = tupleStr + "number_of_lines:- ";
                                    }
                                }
                                if (jm.getSizeInChars() != antMethod.getSizeInChars()) {
                                    if (jm.getSizeInChars() > antMethod.getSizeInChars()) {
                                        tupleStrBuilder.append("size_in_chars:+ ");
                                        //tupleStr = tupleStr + "size_in_chars:+ ";
                                    } else {
                                        tupleStrBuilder.append("size_in_chars:- ");
                                        //tupleStr = tupleStr + "size_in_chars:- ";
                                    }
                                }
                                if (jm.getNumberOfLocalVariables() != antMethod.getNumberOfLocalVariables()) {
                                    if (jm.getNumberOfLocalVariables() > antMethod.getNumberOfLocalVariables()) {
                                        tupleStrBuilder.append("number_of_local_variables:+ ");
                                        //tupleStr = tupleStr + "number_of_local_variables:+ ";
                                    } else {
                                        tupleStrBuilder.append("number_of_local_variables:- ");
                                        //tupleStr = tupleStr + "number_of_local_variables:- ";
                                    }
                                }
                                if (jm.getCyclomaticComplexity() != antMethod.getCyclomaticComplexity()) {
                                    if (jm.getCyclomaticComplexity() > antMethod.getCyclomaticComplexity()) {
                                        tupleStrBuilder.append("cyclomatic_complexity:+ ");
                                        //tupleStr = tupleStr + "cyclomatic_complexity:+ ";
                                    } else {
                                        tupleStrBuilder.append("cyclomatic_complexity:- ");
                                        //tupleStr = tupleStr + "cyclomatic_complexity:- ";
                                    }
                                }
                                if (jm.getMethodInvocations().size() != antMethod.getMethodInvocations().size()) {
                                    if (jm.getMethodInvocations().size() > antMethod.getMethodInvocations().size()) {
                                        tupleStrBuilder.append("number_of_external_method_invocations:+ ");
                                        //tupleStr = tupleStr + "number_of_external_method_invocations:+ ";
                                    } else {
                                        tupleStrBuilder.append("number_of_external_method_invocations:- ");
                                        //tupleStr = tupleStr + "number_of_external_method_invocations:- ";
                                    }
                                }
                                if (jm.getAccessToForeignDataNumber() != antMethod.getAccessToForeignDataNumber()) {
                                    if (jm.getAccessToForeignDataNumber() > antMethod.getAccessToForeignDataNumber()) {
                                        tupleStrBuilder.append("access_to_foreign_data_number:+ ");
                                        //tupleStr = tupleStr + "access_to_foreign_data_number:+ ";
                                    } else {
                                        tupleStrBuilder.append("access_to_foreign_data_number:- ");
                                        //tupleStr = tupleStr + "access_to_foreign_data_number:- ";
                                    }
                                }
                                if (jm.getAccessToLocalDataNumber() != antMethod.getAccessToLocalDataNumber()) {
                                    if (jm.getAccessToLocalDataNumber() > antMethod.getAccessToLocalDataNumber()) {
                                        tupleStrBuilder.append("access_to_local_data_number:+ ");
                                        //tupleStr = tupleStr + "access_to_local_data_number:+ ";
                                    } else {
                                        tupleStrBuilder.append("access_to_local_data_number:- ");
                                        //tupleStr = tupleStr + "access_to_local_data_number:- ";
                                    }
                                }
                                if (jm.getChangingClassesMetric() != antMethod.getChangingClassesMetric()) {
                                    if (jm.getChangingClassesMetric() > antMethod.getChangingClassesMetric()) {
                                        tupleStrBuilder.append("change_classes_metric:+ ");
                                        //tupleStr = tupleStr + "change_classes_metric:+ ";
                                    } else {
                                        tupleStrBuilder.append("change_classes_metric:- ");
                                        //tupleStr = tupleStr + "change_classes_metric:- ";
                                    }
                                }
                                if (jm.getChangingMethods().size() != antMethod.getChangingMethods().size()) {
                                    if (jm.getChangingMethods().size() > antMethod.getChangingMethods().size()) {
                                        tupleStrBuilder.append("number_of_changing_methods:+ ");
                                        //tupleStr = tupleStr + "number_of_changing_methods:+ ";
                                    } else {
                                        tupleStrBuilder.append("number_of_changing_methods:- ");
                                        //tupleStr = tupleStr + "number_of_changing_methods:- ";
                                    }
                                }
                                if (jm.getChangingMethodsMetric() != antMethod.getChangingMethodsMetric()) {
                                    if (jm.getChangingMethodsMetric() > antMethod.getChangingMethodsMetric()) {
                                        tupleStrBuilder.append("changing_methods_metric:+ ");
                                        //tupleStr = tupleStr + "changing_methods_metric:+ ";
                                    } else {
                                        tupleStrBuilder.append("changing_methods_metric:- ");
                                        //tupleStr = tupleStr + "changing_methods_metric:- ";
                                    }
                                }
                                if (jm.getForeignDataProviderNumber() != antMethod.getForeignDataProviderNumber()) {
                                    if (jm.getForeignDataProviderNumber() > antMethod.getForeignDataProviderNumber()) {
                                        tupleStrBuilder.append("foreign_data_provider_number:+ ");
                                        //tupleStr = tupleStr + "foreign_data_provider_number:+ ";
                                    } else {
                                        tupleStrBuilder.append("foreign_data_provider_number:- ");
                                        //tupleStr = tupleStr + "foreign_data_provider_number:- ";
                                    }
                                }
                                if (jm.getInternalMethodInvocations().size() != antMethod.getInternalMethodInvocations().size()) {
                                    if (jm.getInternalMethodInvocations().size() > antMethod.getInternalMethodInvocations().size()) {
                                        tupleStrBuilder.append("number_of_internal_method_invocations:+ ");
                                        //tupleStr = tupleStr + "number_of_internal_method_invocations:+ ";
                                    } else {
                                        tupleStrBuilder.append("number_of_internal_method_invocations:- ");
                                        //tupleStr = tupleStr + "number_of_internal_method_invocations:- ";
                                    }
                                }
                                if (jm.getJavaExternalAttributeAccessList().size() != antMethod.getJavaExternalAttributeAccessList().size()) {
                                    if (jm.getJavaExternalAttributeAccessList().size() > antMethod.getJavaExternalAttributeAccessList().size()) {
                                        tupleStrBuilder.append("number_of_external_access_attribute:+ ");
                                        //tupleStr = tupleStr + "number_of_external_access_attribute:+ ";
                                    } else {
                                        tupleStrBuilder.append("number_of_external_access_attribute:- ");
                                        //tupleStr = tupleStr + "number_of_external_access_attribute:- ";
                                    }
                                }
                                if (!jm.getReturnType().getFullQualifiedName().equals(antMethod.getReturnType().getFullQualifiedName())) {
                                    tupleStrBuilder.append("change_return_type:+ ");
                                    //tupleStr = tupleStr + "change_return_type:+ ";
                                }

                                if (jm.isAbstract() != antMethod.isAbstract()) {
                                    if (jm.isAbstract()) {
                                        tupleStrBuilder.append("change_to_abstract:+ ");
                                        //tupleStr = tupleStr + "change_to_abstract:+ ";
                                    } else {
                                        tupleStrBuilder.append("change_to_not_abstract:+ ");
                                        //tupleStr = tupleStr + "change_to_not_abstract:+ ";
                                    }
                                }
                                if (jm.isPublic() != antMethod.isPublic()) {
                                    if (jm.isPublic()) {
                                        tupleStrBuilder.append("change_to_public:+ ");
                                        //tupleStr = tupleStr + "change_to_public:+ ";
                                    } else {
                                        
                                        if (jm.isPrivate()) {
                                            tupleStrBuilder.append("change_to_private:+ ");
                                            //tupleStr = tupleStr + "change_to_private:+ ";
                                        } else {
                                            tupleStrBuilder.append("change_to_protected:+ ");
                                            //tupleStr = tupleStr + "change_to_protected:+ ";
                                        }
                                    }
                                }
                                if (jm.isPrivate() != antMethod.isPrivate()) {
                                    if (jm.isPrivate()) {
                                        tupleStrBuilder.append("change_to_private:+ ");
                                        //tupleStr = tupleStr + "change_to_private:+ ";
                                    } else {
                                        if (jm.isPublic()) {
                                            tupleStrBuilder.append("change_to_public:+ ");
                                            //tupleStr = tupleStr + "change_to_public:+ ";
                                        } else {
                                            tupleStrBuilder.append("change_to_protected:+ ");
                                            //tupleStr = tupleStr + "change_to_protected:+ ";
                                        }
                                    }
                                }

                                if (jm.isStatic() != antMethod.isStatic()) {
                                    if (jm.isStatic()) {
                                        tupleStrBuilder.append("change_to_static:+ ");
                                        //tupleStr = tupleStr + "change_to_static:+ ";
                                    } else {
                                        tupleStrBuilder.append("change_to_not_static:+ ");
                                        //tupleStr = tupleStr + "change_to_not_static:+ ";
                                    }
                                }

                                if (jm.isFinal() != antMethod.isFinal()) {
                                    if (jm.isFinal()) {
                                        tupleStrBuilder.append("change_to_final:+ ");
                                        //tupleStr = tupleStr + "change_to_final:+ ";
                                    } else {
                                        tupleStrBuilder.append("change_to_not_final:+ ");
                                        //tupleStr = tupleStr + "change_to_not_final:+ ";
                                    }
                                }

                                if (jm.isSynchronized() != antMethod.isSynchronized()) {
                                    if (jm.isSynchronized()) {
                                        tupleStrBuilder.append("change_to_synchronized:+ ");
                                        //tupleStr = tupleStr + "change_to_synchronized:+ ";
                                    } else {
                                        tupleStrBuilder.append("change_to_not_synchronized:+ ");
                                        //tupleStr = tupleStr + "change_to_not_synchronized:+ ";
                                    }
                                }

                                if (jm.isAnAcessorMethod() != antMethod.isAnAcessorMethod()) {
                                    if (jm.isAnAcessorMethod()) {
                                        tupleStrBuilder.append("change_to_accessor_method:+ ");
                                        //tupleStr = tupleStr + "change_to_accessor_method:+ ";
                                    } else {
                                        tupleStrBuilder.append("change_to_not_accessor_method:+ ");
                                        //tupleStr = tupleStr + "change_to_not_accessor_method:+ ";
                                    }
                                }

                                /*if (!jm.isAnAcessorMethod() && antMethod.isAnAcessorMethod()) {
                                 if (!jm.getAccessedAttribute().equals(antMethod.getAccessedAttribute())) {
                                 tupleStr = tupleStr + "change_to_accessor_method:+ ";
                                 } 
                                 }*/

                                if (jm.isChangeInternalState() != antMethod.isChangeInternalState()) {
                                    if (jm.isChangeInternalState()) {
                                        tupleStrBuilder.append("change_to_change_internal_state:+ ");
                                        //tupleStr = tupleStr + "change_to_change_internal_state:+ ";
                                    } else {
                                        tupleStrBuilder.append("change_to_not_change_internal_state:+ ");
                                        //tupleStr = tupleStr + "change_to_not_change_internal_state:+ ";
                                    }
                                }

                                if (jm.isChangeInternalStateByMethodInvocations() != antMethod.isChangeInternalStateByMethodInvocations()) {
                                    if (jm.isChangeInternalState()) {
                                        tupleStrBuilder.append("change_to_change_internal_state_by_method_invocation:+ ");
                                        //tupleStr = tupleStr + "change_to_change_internal_state_by_method_invocation:+ ";
                                    } else {
                                        tupleStrBuilder.append("change_to_not_change_internal_state_by_method_invocation:+ ");
                                        //tupleStr = tupleStr + "change_to_not_change_internal_state_by_method_invocation:+ ";
                                    }
                                }



                                //verificar o mesmo para a classe interna:

                                List<String> auxClassesChangesList = getClassesChanges(antClass, jc);
                                for (String auxStr : auxClassesChangesList) {
                                    String[] strArray = auxStr.split(":");
                                    
                                    auxStr = strArray[0] + "_in_same_class_of_this_method" + ":" + strArray[1];
                                    tupleStrBuilder.append(auxStr);
                                    //tupleStr = tupleStr + auxStr;
                                }





                                //verificar o mesmo para os métodos que ele utiliza diretamente dentro da classe
                                HashMap<String, String> hashMapAux = new HashMap();
                                List<JavaMethod> methodsFromAnt = new LinkedList();
                                List<JavaMethod> methodsFromNow = new LinkedList();
                                List<JavaMethod> methodsFromAntAndNow = new LinkedList();
                                for (JavaMethod jmAuxNow : jm.getInternalMethodInvocations()) {
                                    if (jmAuxNow != null) {
                                        boolean encontrou = false;
                                        for (JavaMethod jmAuxAnt : antMethod.getInternalMethodInvocations()) {
                                            if (jmAuxAnt != null && jmAuxAnt.getMethodSignature().equals(jmAuxNow.getMethodSignature())) {
                                                encontrou = true;
                                                break;
                                            }
                                        }
                                        if (encontrou) {
                                            methodsFromAntAndNow.add(jmAuxNow);
                                        } else {
                                            methodsFromNow.add(jmAuxNow);
                                        }
                                    }
                                }
                                for (JavaMethod jmAuxAnt : antMethod.getInternalMethodInvocations()) {
                                    if (jmAuxAnt != null) {
                                        boolean encontrou = false;
                                        for (JavaMethod jmAuxNow : jm.getInternalMethodInvocations()) {
                                            if (jmAuxNow != null && jmAuxAnt.getMethodSignature().equals(jmAuxNow.getMethodSignature())) {
                                                encontrou = true;
                                                break;
                                            }
                                        }
                                        if (!encontrou) {
                                            methodsFromAnt.add(jmAuxAnt);
                                        }
                                    }
                                }

                                for (JavaMethod jmAuxAnt : methodsFromAnt) {
                                    JavaMethod jmAux = jc.getMethodBySignature(jm.getMethodSignature());
                                    if (jmAux != null) {
                                        List<String> auxList = getMethodsChanges(jmAuxAnt, jmAux, antClass);
                                        for (String auxStr : auxList) {
                                            String[] strArray = auxStr.split(":");
                                            auxStr = strArray[0] + "_in_other_method_in_same_class_that_this_method_previously_called" + ":" + strArray[1];
                                            if (hashMapAux.get(auxStr) == null) {
                                                hashMapAux.put(auxStr, auxStr);
                                                tupleStrBuilder.append(auxStr);
                                                //tupleStr = tupleStr + auxStr;
                                            }
                                        }
                                    }
                                }

                                for (JavaMethod jmAux : methodsFromNow) {
                                    JavaMethod jmAuxAnt = antClass.getMethodBySignature(jm.getMethodSignature());
                                    if (jmAuxAnt != null) {
                                        List<String> auxList = getMethodsChanges(jmAuxAnt, jmAux, antClass);
                                        for (String auxStr : auxList) {
                                            String[] strArray = auxStr.split(":");
                                            auxStr = strArray[0] + "_in_other_method_in_same_class_that_this_method_from_now_call" + ":" + strArray[1];
                                            if (hashMapAux.get(auxStr) == null) {
                                                hashMapAux.put(auxStr, auxStr);
                                                tupleStrBuilder.append(auxStr);
                                                //tupleStr = tupleStr + auxStr;
                                            }
                                        }
                                    }
                                }


                                for (JavaMethod jmAux : methodsFromAntAndNow) {
                                    JavaMethod jmAuxAnt = antClass.getMethodBySignature(jm.getMethodSignature());
                                    List<String> auxList = getMethodsChanges(jmAuxAnt, jmAux, antClass);
                                    for (String auxStr : auxList) {
                                        String[] strArray = auxStr.split(":");
                                        auxStr = strArray[0] + "_in_other_method_in_same_class_that_this_method_call" + ":" + strArray[1];
                                        if (hashMapAux.get(auxStr) == null) {
                                            hashMapAux.put(auxStr, auxStr);
                                            tupleStrBuilder.append(auxStr);
                                            //tupleStr = tupleStr + auxStr;
                                        }
                                    }
                                }




                                /*for (JavaMethod jmAux : jm.getInternalMethodInvocations()) {

                                 List<String> auxList = getMethodsChanges(jmAux, antClass);
                                 for (String auxStr : auxList) {
                                 String[] strArray = auxStr.split(":");
                                 auxStr = strArray[0] + "_in_other_method_in_same_class_that_this_method_call" + ":" + strArray[1];
                                 if (hashMapAux.get(auxStr) == null) {
                                 hashMapAux.put(auxStr, auxStr);
                                 tupleStr = tupleStr + auxStr;
                                 }
                                 }
                                 }*/
                                //verificar o mesmo para os métodos que o utilizam diretamente dentro da classe
                                hashMapAux = new HashMap();
                                methodsFromAnt = new LinkedList();
                                methodsFromNow = new LinkedList();
                                methodsFromAntAndNow = new LinkedList();
                                for (JavaMethod jmAuxNow : jm.getInternalMethodsThatCallMe()) {
                                    boolean encontrou = false;
                                    for (JavaMethod jmAuxAnt : antMethod.getInternalMethodsThatCallMe()) {
                                        if (jmAuxAnt.getMethodSignature().equals(jmAuxNow.getMethodSignature())) {
                                            encontrou = true;
                                            break;
                                        }
                                    }
                                    if (encontrou) {
                                        methodsFromAntAndNow.add(jmAuxNow);
                                    } else {
                                        methodsFromNow.add(jmAuxNow);
                                    }
                                }
                                for (JavaMethod jmAuxAnt : antMethod.getInternalMethodsThatCallMe()) {
                                    boolean encontrou = false;
                                    for (JavaMethod jmAuxNow : jm.getInternalMethodsThatCallMe()) {
                                        if (jmAuxAnt.getMethodSignature().equals(jmAuxNow.getMethodSignature())) {
                                            encontrou = true;
                                            break;
                                        }
                                    }
                                    if (!encontrou) {
                                        methodsFromAnt.add(jmAuxAnt);
                                    }
                                }

                                for (JavaMethod jmAuxAnt : methodsFromAnt) {
                                    JavaMethod jmAux = jc.getMethodBySignature(jm.getMethodSignature());
                                    if (jmAux != null) {
                                        List<String> auxList = getMethodsChanges(jmAuxAnt, jmAux, antClass);
                                        for (String auxStr : auxList) {
                                            String[] strArray = auxStr.split(":");
                                            auxStr = strArray[0] + "_in_other_method_in_same_class_that_previously_called_me" + ":" + strArray[1];
                                            if (hashMapAux.get(auxStr) == null) {
                                                hashMapAux.put(auxStr, auxStr);
                                                tupleStrBuilder.append(auxStr);
                                                //tupleStr = tupleStr + auxStr;
                                            }
                                        }
                                    }
                                }

                                for (JavaMethod jmAux : methodsFromNow) {
                                    JavaMethod jmAuxAnt = antClass.getMethodBySignature(jm.getMethodSignature());
                                    if (jmAuxAnt != null) {
                                        List<String> auxList = getMethodsChanges(jmAuxAnt, jmAux, antClass);
                                        for (String auxStr : auxList) {
                                            String[] strArray = auxStr.split(":");
                                            auxStr = strArray[0] + "_in_other_method_in_same_class_that_from_now_call_me" + ":" + strArray[1];
                                            if (hashMapAux.get(auxStr) == null) {
                                                hashMapAux.put(auxStr, auxStr);
                                                tupleStrBuilder.append(auxStr);
                                                //tupleStr = tupleStr + auxStr;
                                            }
                                        }
                                    }
                                }


                                for (JavaMethod jmAux : methodsFromAntAndNow) {
                                    JavaMethod jmAuxAnt = antClass.getMethodBySignature(jm.getMethodSignature());
                                    List<String> auxList = getMethodsChanges(jmAuxAnt, jmAux, antClass);
                                    for (String auxStr : auxList) {
                                        String[] strArray = auxStr.split(":");
                                        auxStr = strArray[0] + "_in_other_method_in_same_class_that_call_me" + ":" + strArray[1];
                                        if (hashMapAux.get(auxStr) == null) {
                                            hashMapAux.put(auxStr, auxStr);
                                            tupleStrBuilder.append(auxStr);
                                            //tupleStr = tupleStr + auxStr;
                                        }
                                    }
                                }


                                //verificar o mesmo para os métodos que eles invocam externamente:
                                hashMapAux = new HashMap();
                                HashMap<String, String> hashMapOfClasses = new HashMap();
                                methodsFromAnt = new LinkedList();
                                methodsFromNow = new LinkedList();
                                methodsFromAntAndNow = new LinkedList();

                                for (JavaMethodInvocation jmiNow : jm.getMethodInvocations()) {

                                    JavaMethod jmAuxNow = jmiNow.getJavaMethod();
                                    if (jmAuxNow != null) {
                                        boolean encontrou = false;

                                        for (JavaMethodInvocation jmiAnt : antMethod.getMethodInvocations()) {
                                            JavaMethod jmAuxAnt = jmiAnt.getJavaMethod();
                                            if (jmAuxAnt != null) {
                                                if (jmAuxAnt.getMethodSignature().equals(jmAuxNow.getMethodSignature())) {
                                                    encontrou = true;
                                                    break;
                                                }
                                            }
                                        }
                                        if (encontrou) {
                                            methodsFromAntAndNow.add(jmAuxNow);
                                        } else {
                                            methodsFromNow.add(jmAuxNow);
                                        }
                                    }
                                }
                                for (JavaMethodInvocation jmiAnt : antMethod.getMethodInvocations()) {

                                    JavaMethod jmAuxAnt = jmiAnt.getJavaMethod();
                                    if (jmAuxAnt != null) {
                                        boolean encontrou = false;

                                        for (JavaMethodInvocation jmiNow : jm.getMethodInvocations()) {
                                            JavaMethod jmAuxNow = jmiAnt.getJavaMethod();
                                            if (jmAuxAnt != null) {
                                                if (jmAuxAnt.getMethodSignature().equals(jmAuxNow.getMethodSignature())) {
                                                    encontrou = true;
                                                    break;
                                                }
                                            }
                                        }
                                        if (!encontrou) {
                                            methodsFromAnt.add(jmAuxAnt);
                                        }
                                    }
                                }

                                for (JavaMethod jmAuxAnt : methodsFromAnt) {
                                    JavaAbstract jaAux = jp.getClassByName(jmAuxAnt.getJavaAbstract().getFullQualifiedName());
                                    if (jaAux != null && jaAux.getClass() == JavaClass.class) {
                                        JavaClass jcAux = (JavaClass) jaAux;
                                        JavaMethod jmAux = jcAux.getMethodBySignature(jm.getMethodSignature());
                                        if (jmAux != null) {
                                            //verificando o de método
                                            List<String> auxList = getMethodsChanges(jmAuxAnt, jmAux, antClass);
                                            for (String auxStr : auxList) {
                                                String[] strArray = auxStr.split(":");
                                                auxStr = strArray[0] + "_in_other_method_in_other_class_that_this_method_previously_called" + ":" + strArray[1];
                                                if (hashMapAux.get(auxStr) == null) {
                                                    hashMapAux.put(auxStr, auxStr);
                                                    tupleStrBuilder.append(auxStr);
                                                    //tupleStr = tupleStr + auxStr;
                                                }
                                            }
                                            //verificando o de classe

                                            if (hashMapOfClasses.get(jcAux.getFullQualifiedName()) == null) {
                                                hashMapOfClasses.put(jcAux.getFullQualifiedName(), jcAux.getFullQualifiedName());
                                                auxList = getMethodsChanges(jmAuxAnt, jmAux, antClass);
                                                for (String auxStr : auxList) {
                                                    String[] strArray = auxStr.split(":");
                                                    auxStr = strArray[0] + "_in_other_class_that_this_method_previously_called" + ":" + strArray[1];
                                                    if (hashMapAux.get(auxStr) == null) {
                                                        hashMapAux.put(auxStr, auxStr);
                                                        tupleStrBuilder.append(auxStr);
                                                        //tupleStr = tupleStr + auxStr;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                hashMapOfClasses = new HashMap();

                                for (JavaMethod jmAux : methodsFromNow) {
                                    JavaAbstract jaAux = ant.getClassByName(jmAux.getJavaAbstract().getFullQualifiedName());
                                    if (jaAux != null && jaAux.getClass() == JavaClass.class) {
                                        JavaClass jcAux = (JavaClass) jaAux;
                                        JavaMethod jmAuxAnt = jcAux.getMethodBySignature(jm.getMethodSignature());
                                        if (jmAuxAnt != null) {
                                            List<String> auxList = getMethodsChanges(jmAuxAnt, jmAux, antClass);
                                            for (String auxStr : auxList) {
                                                String[] strArray = auxStr.split(":");
                                                auxStr = strArray[0] + "_in_other_method_in_other_class_that_this_method_from_now_call" + ":" + strArray[1];
                                                if (hashMapAux.get(auxStr) == null) {
                                                    hashMapAux.put(auxStr, auxStr);
                                                    tupleStrBuilder.append(auxStr);
                                                    //tupleStr = tupleStr + auxStr;
                                                }
                                            }

                                            //verificando o de classe

                                            if (hashMapOfClasses.get(jcAux.getFullQualifiedName()) == null) {
                                                hashMapOfClasses.put(jcAux.getFullQualifiedName(), jcAux.getFullQualifiedName());
                                                auxList = getMethodsChanges(jmAuxAnt, jmAux, antClass);
                                                for (String auxStr : auxList) {
                                                    String[] strArray = auxStr.split(":");
                                                    auxStr = strArray[0] + "_in_class_that_this_method_from_now_call" + ":" + strArray[1];
                                                    if (hashMapAux.get(auxStr) == null) {
                                                        hashMapAux.put(auxStr, auxStr);
                                                        tupleStrBuilder.append(auxStr);
                                                        //tupleStr = tupleStr + auxStr;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                hashMapOfClasses = new HashMap();

                                for (JavaMethod jmAux : methodsFromAntAndNow) {
                                    JavaAbstract jaAux = ant.getClassByName(jmAux.getJavaAbstract().getFullQualifiedName());
                                    if (jaAux != null && jaAux.getClass() == JavaClass.class) {
                                        JavaClass jcAux = (JavaClass) jaAux;
                                        JavaMethod jmAuxAnt = jcAux.getMethodBySignature(jm.getMethodSignature());
                                        List<String> auxList = getMethodsChanges(jmAuxAnt, jmAux, antClass);
                                        for (String auxStr : auxList) {
                                            String[] strArray = auxStr.split(":");
                                            auxStr = strArray[0] + "_in_other_method_in_other_class_that_this_method_call" + ":" + strArray[1];
                                            if (hashMapAux.get(auxStr) == null) {
                                                hashMapAux.put(auxStr, auxStr);
                                                tupleStrBuilder.append(auxStr);
                                                //tupleStr = tupleStr + auxStr;
                                            }
                                        }

                                        //verificando o de classe

                                        if (hashMapOfClasses.get(jcAux.getFullQualifiedName()) == null) {
                                            hashMapOfClasses.put(jcAux.getFullQualifiedName(), jcAux.getFullQualifiedName());
                                            auxList = getMethodsChanges(jmAuxAnt, jmAux, antClass);
                                            for (String auxStr : auxList) {
                                                String[] strArray = auxStr.split(":");
                                                auxStr = strArray[0] + "_in_other_class_that_this_method_call" + ":" + strArray[1];
                                                if (hashMapAux.get(auxStr) == null) {
                                                    hashMapAux.put(auxStr, auxStr);
                                                    tupleStrBuilder.append(auxStr);
                                                    //tupleStr = tupleStr + auxStr;
                                                }
                                            }
                                        }
                                    }
                                }

                                //verificar o mesmo para as classes que me chamam (changing methods)
                                hashMapAux = new HashMap();
                                hashMapOfClasses = new HashMap();
                                methodsFromAnt = new LinkedList();
                                methodsFromNow = new LinkedList();
                                methodsFromAntAndNow = new LinkedList();
                                for (JavaMethod jmAuxNow : jm.getChangingMethods()) {
                                    boolean encontrou = false;
                                    for (JavaMethod jmAuxAnt : antMethod.getChangingMethods()) {
                                        if (jmAuxAnt.getMethodSignature().equals(jmAuxNow.getMethodSignature())) {
                                            encontrou = true;
                                            break;
                                        }
                                    }
                                    if (encontrou) {
                                        methodsFromAntAndNow.add(jmAuxNow);
                                    } else {
                                        methodsFromNow.add(jmAuxNow);
                                    }
                                }
                                for (JavaMethod jmAuxAnt : antMethod.getChangingMethods()) {
                                    boolean encontrou = false;
                                    for (JavaMethod jmAuxNow : jm.getChangingMethods()) {
                                        if (jmAuxAnt.getMethodSignature().equals(jmAuxNow.getMethodSignature())) {
                                            encontrou = true;
                                            break;
                                        }
                                    }
                                    if (!encontrou) {
                                        methodsFromAnt.add(jmAuxAnt);
                                    }
                                }

                                for (JavaMethod jmAuxAnt : methodsFromAnt) {
                                    JavaAbstract jaAux = jp.getClassByName(jmAuxAnt.getJavaAbstract().getFullQualifiedName());
                                    if (jaAux != null && jaAux.getClass() == JavaClass.class) {
                                        JavaClass jcAux = (JavaClass) jaAux;
                                        JavaMethod jmAux = jcAux.getMethodBySignature(jm.getMethodSignature());
                                        if (jmAux != null) {
                                            List<String> auxList = getMethodsChanges(jmAuxAnt, jmAux, antClass);
                                            for (String auxStr : auxList) {
                                                String[] strArray = auxStr.split(":");
                                                auxStr = strArray[0] + "_in_other_method_in_other_class_that_previously_called_me" + ":" + strArray[1];
                                                if (hashMapAux.get(auxStr) == null) {
                                                    hashMapAux.put(auxStr, auxStr);
                                                    tupleStrBuilder.append(auxStr);
                                                    //tupleStr = tupleStr + auxStr;
                                                }
                                            }

                                            //verificando o de classe

                                            if (hashMapOfClasses.get(jcAux.getFullQualifiedName()) == null) {
                                                hashMapOfClasses.put(jcAux.getFullQualifiedName(), jcAux.getFullQualifiedName());
                                                auxList = getMethodsChanges(jmAuxAnt, jmAux, antClass);
                                                for (String auxStr : auxList) {
                                                    String[] strArray = auxStr.split(":");
                                                    auxStr = strArray[0] + "_in_other_class_that_previously_called_me" + ":" + strArray[1];
                                                    if (hashMapAux.get(auxStr) == null) {
                                                        hashMapAux.put(auxStr, auxStr);
                                                        tupleStrBuilder.append(auxStr);
                                                        //tupleStr = tupleStr + auxStr;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                hashMapOfClasses = new HashMap();
                                for (JavaMethod jmAux : methodsFromNow) {
                                    JavaAbstract jaAux = ant.getClassByName(jmAux.getJavaAbstract().getFullQualifiedName());
                                    if (jaAux != null && jaAux.getClass() == JavaClass.class) {
                                        JavaClass jcAux = (JavaClass) jaAux;
                                        JavaMethod jmAuxAnt = jcAux.getMethodBySignature(jm.getMethodSignature());
                                        if (jmAuxAnt != null) {
                                            List<String> auxList = getMethodsChanges(jmAuxAnt, jmAux, antClass);
                                            for (String auxStr : auxList) {
                                                String[] strArray = auxStr.split(":");
                                                auxStr = strArray[0] + "_in_other_method_in_other_class_that_from_now_call_me" + ":" + strArray[1];
                                                if (hashMapAux.get(auxStr) == null) {
                                                    hashMapAux.put(auxStr, auxStr);
                                                    tupleStrBuilder.append(auxStr);
                                                    //tupleStr = tupleStr + auxStr;
                                                }
                                            }

                                            //verificando o de classe

                                            if (hashMapOfClasses.get(jcAux.getFullQualifiedName()) == null) {
                                                hashMapOfClasses.put(jcAux.getFullQualifiedName(), jcAux.getFullQualifiedName());
                                                auxList = getMethodsChanges(jmAuxAnt, jmAux, antClass);
                                                for (String auxStr : auxList) {
                                                    String[] strArray = auxStr.split(":");
                                                    auxStr = strArray[0] + "_in_class_that_from_now_call_me" + ":" + strArray[1];
                                                    if (hashMapAux.get(auxStr) == null) {
                                                        hashMapAux.put(auxStr, auxStr);
                                                        tupleStrBuilder.append(auxStr);
                                                        //tupleStr = tupleStr + auxStr;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }


                                hashMapOfClasses = new HashMap();
                                for (JavaMethod jmAux : methodsFromAntAndNow) {
                                    JavaAbstract jaAux = ant.getClassByName(jmAux.getJavaAbstract().getFullQualifiedName());
                                    if (jaAux != null && jaAux.getClass() == JavaClass.class) {
                                        JavaClass jcAux = (JavaClass) jaAux;
                                        JavaMethod jmAuxAnt = jcAux.getMethodBySignature(jm.getMethodSignature());
                                        List<String> auxList = getMethodsChanges(jmAuxAnt, jmAux, antClass);
                                        for (String auxStr : auxList) {
                                            String[] strArray = auxStr.split(":");
                                            auxStr = strArray[0] + "_in_other_method_in_other_class_that_call_me" + ":" + strArray[1];
                                            if (hashMapAux.get(auxStr) == null) {
                                                hashMapAux.put(auxStr, auxStr);
                                                tupleStrBuilder.append(auxStr);
                                                //tupleStr = tupleStr + auxStr;
                                            }
                                        }

                                        //verificando o de classe

                                        if (hashMapOfClasses.get(jcAux.getFullQualifiedName()) == null) {
                                            hashMapOfClasses.put(jcAux.getFullQualifiedName(), jcAux.getFullQualifiedName());
                                            auxList = getMethodsChanges(jmAuxAnt, jmAux, antClass);
                                            for (String auxStr : auxList) {
                                                String[] strArray = auxStr.split(":");
                                                auxStr = strArray[0] + "_in_other_class_that_call_me" + ":" + strArray[1];
                                                if (hashMapAux.get(auxStr) == null) {
                                                    hashMapAux.put(auxStr, auxStr);
                                                    tupleStrBuilder.append(auxStr);
                                                    //tupleStr = tupleStr + auxStr;
                                                }
                                            }
                                        }
                                    }
                                }

                                //em algum método em geral






                                //começa agora mudanças de padrões (anomalias)
                                boolean changeSameMethod = false;
                                boolean changeOtherMethod = false;
                                //feature envy
                                for (JavaMethod javaMethod : featureEnvyEmerge) {
                                    if (javaMethod.getMethodSignature().equals(jm.getMethodSignature())) {
                                        changeSameMethod = true;
                                        if (changeOtherMethod) {
                                            break;
                                        }
                                    } else {
                                        changeOtherMethod = true;
                                        if (changeSameMethod) {
                                            break;
                                        }
                                    }
                                }
                                if (changeSameMethod) {
                                    tupleStrBuilder.append("feature_envy_in_this_method_emerge:+ ");
                                    //tupleStr = tupleStr + "feature_envy_in_this_method_emerge:+ ";
                                }
                                if (changeOtherMethod) {
                                    tupleStrBuilder.append("feature_envy_in_another_method_in_same_class_emerge:+ ");
                                    //tupleStr = tupleStr + "feature_envy_in_another_method_in_same_class_emerge:+ ";
                                }
                                changeSameMethod = false;
                                changeOtherMethod = false;
                                for (JavaMethod javaMethod : featureEnvyCorrect) {
                                    if (javaMethod.getMethodSignature().equals(jm.getMethodSignature())) {
                                        changeSameMethod = true;
                                        if (changeOtherMethod) {
                                            break;
                                        }
                                    } else {
                                        changeOtherMethod = true;
                                        if (changeSameMethod) {
                                            break;
                                        }
                                    }
                                }
                                if (changeSameMethod) {
                                    tupleStrBuilder.append("feature_envy_in_this_method_correct:+ ");
                                    //tupleStr = tupleStr + "feature_envy_in_this_method_correct:+ ";
                                }
                                if (changeOtherMethod) {
                                    tupleStrBuilder.append("feature_envy_in_another_method_in_same_class_correct:+ ");
                                    //tupleStr = tupleStr + "feature_envy_in_another_method_in_same_class_correct:+ ";
                                }

                                //shotgun surgery
                                changeSameMethod = false;
                                changeOtherMethod = false;
                                for (JavaMethod javaMethod : shotgunEmerge) {
                                    if (javaMethod.getMethodSignature().equals(jm.getMethodSignature())) {
                                        changeSameMethod = true;
                                        if (changeOtherMethod) {
                                            break;
                                        }
                                    } else {
                                        changeOtherMethod = true;
                                        if (changeSameMethod) {
                                            break;
                                        }
                                    }
                                }
                                if (changeSameMethod) {
                                    tupleStrBuilder.append("shotgun_surgery_in_this_method_emerge:+ ");
                                    //tupleStr = tupleStr + "shotgun_surgery_in_this_method_emerge:+ ";
                                }
                                if (changeOtherMethod) {
                                    tupleStrBuilder.append("shotgun_surgery_in_another_method_in_same_class_emerge:+ ");
                                    //tupleStr = tupleStr + "shotgun_surgery_in_another_method_in_same_class_emerge:+ ";
                                }
                                changeSameMethod = false;
                                changeOtherMethod = false;
                                for (JavaMethod javaMethod : shotgunCorrect) {
                                    if (javaMethod.getMethodSignature().equals(jm.getMethodSignature())) {
                                        changeSameMethod = true;
                                        if (changeOtherMethod) {
                                            break;
                                        }
                                    } else {
                                        changeOtherMethod = true;
                                        if (changeSameMethod) {
                                            break;
                                        }
                                    }
                                }
                                if (changeSameMethod) {
                                    tupleStrBuilder.append("shotgun_surgery_in_this_method_correct:+ ");
                                    //tupleStr = tupleStr + "shotgun_surgery_in_this_method_correct:+ ";
                                }
                                if (changeOtherMethod) {
                                    tupleStrBuilder.append("shotgun_surgery_in_another_method_in_same_class_correct:+ ");
                                    //tupleStr = tupleStr + "shotgun_surgery_in_another_method_in_same_class_correct:+ ";
                                }


                                //god method
                                changeSameMethod = false;
                                changeOtherMethod = false;
                                for (JavaMethod javaMethod : godMethodEmerge) {
                                    if (javaMethod.getMethodSignature().equals(jm.getMethodSignature())) {
                                        changeSameMethod = true;
                                        if (changeOtherMethod) {
                                            break;
                                        }
                                    } else {
                                        changeOtherMethod = true;
                                        if (changeSameMethod) {
                                            break;
                                        }
                                    }
                                }
                                if (changeSameMethod) {
                                    tupleStrBuilder.append("god_method_in_this_method_emerge:+ ");
                                    //tupleStr = tupleStr + "god_method_in_this_method_emerge:+ ";
                                }
                                if (changeOtherMethod) {
                                    tupleStrBuilder.append("god_method_in_another_method_in_same_class_emerge:+ ");
                                    //tupleStr = tupleStr + "god_method_in_another_method_in_same_class_emerge:+ ";
                                }
                                changeSameMethod = false;
                                changeOtherMethod = false;
                                for (JavaMethod javaMethod : godMethodCorrect) {
                                    if (javaMethod.getMethodSignature().equals(jm.getMethodSignature())) {
                                        changeSameMethod = true;
                                        if (changeOtherMethod) {
                                            break;
                                        }
                                    } else {
                                        changeOtherMethod = true;
                                        if (changeSameMethod) {
                                            break;
                                        }
                                    }
                                }
                                if (changeSameMethod) {
                                    tupleStrBuilder.append("god_method_in_this_method_correct:+ ");
                                    //tupleStr = tupleStr + "god_method_in_this_method_correct:+ ";
                                }
                                if (changeOtherMethod) {
                                    tupleStrBuilder.append("god_method_in_another_method_in_same_class_correct:+ ");
                                    //tupleStr = tupleStr + "god_method_in_another_method_in_same_class_correct:+ ";
                                }

                                //god class
                                boolean changeSameClass = false;
                                boolean changeOtherClassSamePackage = false;
                                boolean changeOtherClassOtherPackage = false;
                                for (JavaClass javaClass : godClassEmerge) {
                                    if (javaClass.getFullQualifiedName().equals(jc.getFullQualifiedName())) {
                                        changeSameClass = true;
                                    } else {
                                        if (javaClass.getJavaPackage().getName().equals(jc.getJavaPackage().getName())) {
                                            changeOtherClassSamePackage = true;
                                        } else {
                                            changeOtherClassOtherPackage = true;
                                        }
                                    }
                                }
                                if (changeSameClass) {
                                    tupleStrBuilder.append("god_class_in_this_class_emerge:+ ");
                                    //tupleStr = tupleStr + "god_class_in_this_class_emerge:+ ";
                                }
                                if (changeOtherClassSamePackage) {
                                    tupleStrBuilder.append("god_class_in_another_class_in_same_package_emerge:+ ");
                                    //tupleStr = tupleStr + "god_class_in_another_class_in_same_package_emerge:+ ";
                                }
                                if (changeOtherClassOtherPackage) {
                                    tupleStrBuilder.append("god_class_in_another_class_in_another_package_emerge:+ ");
                                    //tupleStr = tupleStr + "god_class_in_another_class_in_another_package_emerge:+ ";
                                }
                                changeSameClass = false;
                                changeOtherClassSamePackage = false;
                                changeOtherClassOtherPackage = false;
                                for (JavaClass javaClass : godClassCorrect) {
                                    if (javaClass.getFullQualifiedName().equals(jc.getFullQualifiedName())) {
                                        changeSameClass = true;

                                    } else {
                                        if (javaClass.getJavaPackage().getName().equals(jc.getJavaPackage().getName())) {
                                            changeOtherClassSamePackage = true;
                                        } else {
                                            changeOtherClassOtherPackage = true;
                                        }
                                    }
                                }
                                if (changeSameClass) {
                                    tupleStrBuilder.append("god_class_in_this_class_correct:+ ");
                                    //tupleStr = tupleStr + "god_class_in_this_class_correct:+ ";
                                }
                                if (changeOtherClassSamePackage) {
                                    tupleStrBuilder.append("god_class_in_another_class_in_same_package_correct:+ ");
                                    //tupleStr = tupleStr + "god_class_in_another_class_in_same_package_correct:+ ";
                                }
                                if (changeOtherClassOtherPackage) {
                                    tupleStrBuilder.append("god_class_in_another_class_in_another_package_correct:+ ");
                                    //tupleStr = tupleStr + "god_class_in_another_class_in_another_package_correct:+ ";
                                }


                                //god misplaced class
                                changeSameClass = false;
                                changeOtherClassSamePackage = false;
                                changeOtherClassOtherPackage = false;
                                for (JavaClass javaClass : misplacedClassEmerge) {
                                    if (javaClass.getFullQualifiedName().equals(jc.getFullQualifiedName())) {
                                        changeSameClass = true;
                                    } else {
                                        if (javaClass.getJavaPackage().getName().equals(jc.getJavaPackage().getName())) {
                                            changeOtherClassSamePackage = true;
                                        } else {
                                            changeOtherClassOtherPackage = true;
                                        }
                                    }
                                }
                                if (changeSameClass) {
                                    tupleStrBuilder.append("misplaced_class_in_this_class_emerge:+ ");
                                    //tupleStr = tupleStr + "misplaced_class_in_this_class_emerge:+ ";
                                }
                                if (changeOtherClassSamePackage) {
                                    tupleStrBuilder.append("misplaced_class_in_another_class_in_same_package_emerge:+ ");
                                    //tupleStr = tupleStr + "misplaced_class_in_another_class_in_same_package_emerge:+ ";
                                }
                                if (changeOtherClassOtherPackage) {
                                    tupleStrBuilder.append("misplaced_class_in_another_class_in_another_package_emerge:+ ");
                                    //tupleStr = tupleStr + "misplaced_class_in_another_class_in_another_package_emerge:+ ";
                                }
                                changeSameClass = false;
                                changeOtherClassSamePackage = false;
                                changeOtherClassOtherPackage = false;
                                for (JavaClass javaClass : misplacedClassCorrect) {
                                    if (javaClass.getFullQualifiedName().equals(jc.getFullQualifiedName())) {
                                        changeSameClass = true;

                                    } else {
                                        if (javaClass.getJavaPackage().getName().equals(jc.getJavaPackage().getName())) {
                                            changeOtherClassSamePackage = true;
                                        } else {
                                            changeOtherClassOtherPackage = true;
                                        }
                                    }
                                }
                                if (changeSameClass) {
                                    tupleStrBuilder.append("misplaced_class_in_this_class_correct:+ ");
                                    //tupleStr = tupleStr + "misplaced_class_in_this_class_correct:+ ";
                                }
                                if (changeOtherClassSamePackage) {
                                    tupleStrBuilder.append("misplaced_class_in_another_class_in_same_package_correct:+ ");
                                    //tupleStr = tupleStr + "misplaced_class_in_another_class_in_same_package_correct:+ ";
                                }
                                if (changeOtherClassOtherPackage) {
                                    tupleStrBuilder.append("misplaced_class_in_another_class_in_another_package_correct:+ ");
                                    //tupleStr = tupleStr + "misplaced_class_in_another_class_in_another_package_correct:+ ";
                                }


                                //god package
                                boolean changeSamePackage = false;
                                boolean changeOtherPackage = false;
                                for (JavaPackage javaPackage : godPackageEmerge) {
                                    if (javaPackage.getName().equals(jc.getJavaPackage().getName())) {
                                        changeSamePackage = true;
                                        if (changeOtherPackage) {
                                            break;
                                        }
                                    } else {
                                        changeOtherPackage = true;
                                        if (changeSamePackage) {
                                            break;
                                        }
                                    }
                                }
                                if (changeSamePackage) {
                                    tupleStrBuilder.append("god_package_in_same_package_emerge:+ ");
                                    //tupleStr = tupleStr + "god_package_in_same_package_emerge:+ ";
                                }
                                if (changeOtherPackage) {
                                    tupleStrBuilder.append("god_package_in_another_package_emerge:+ ");
                                    //tupleStr = tupleStr + "god_package_in_another_package_emerge:+ ";
                                }
                                changeSamePackage = false;
                                changeOtherPackage = false;
                                for (JavaPackage javaPackage : godPackageCorrect) {
                                    if (javaPackage.getName().equals(jc.getJavaPackage().getName())) {
                                        changeSamePackage = true;
                                        if (changeOtherPackage) {
                                            break;
                                        }
                                    } else {
                                        changeOtherPackage = true;
                                        if (changeSamePackage) {
                                            break;
                                        }
                                    }
                                }
                                if (changeSamePackage) {
                                    tupleStrBuilder.append("god_package_in_same_package_correct:+ ");
                                    //tupleStr = tupleStr + "god_package_in_same_package_correct:+ ";
                                }
                                if (changeOtherPackage) {
                                    tupleStrBuilder.append("god_package_in_another_package_correct:+ ");
                                    //tupleStr = tupleStr + "god_package_in_another_package_correct:+ ";
                                }




                                String tupleStr = tupleStrBuilder.toString();
                                if (!tupleStr.equals("")) {
                                    tupleStr = tupleStr.substring(0, tupleStr.length() - 1);
                                    String filePaths[] = temporaryFileManager.getFilePath(jc.getFullQualifiedName() + ":" + jm.getMethodSignature());
                                    String filePath = filePaths[0];
                                    String fileRevisionPath = filePaths[1];
                                    try{
                                        FileWriter fw = new FileWriter(filePath,true); //the true will append the new data
                                        fw.write(tupleStr+"\n");//appends the string to the file
                                        fw.close();
                                        fw = new FileWriter(fileRevisionPath,true); //the true will append the new data
                                        fw.write(antRevision.getId()+" "+rev.getId()+"\n");//appends the string to the file
                                        fw.close();
                                    }catch(Exception e){
                                        System.out.println("Erro escrita de dados de mineração: "+e.getMessage());
                                        e.printStackTrace();
                                    }
                                    
                                    
                                    
                                    //sequence.addItem(tupleStr);
                                }/*
                                if (!tupleStrBuilder.toString().equals("")) {
                                    tupleStr = tupleStr.substring(0, tupleStr.length() - 1);
                                    sequence.addItem(tupleStr);
                                }*/


                            } else {
                                //colocar aqui quando o método surge
                            }
                        }
                    }
                }
            }
            ant = jp;
            if (rev.getNext().size() == 0) {
                rev = null;
            } else {
                antRevision = rev;
                rev = rev.getNext().get(0);
            }

        }


        /*Collection<Sequence> sequenceCollection = hashMap.values();
        Iterator it = sequenceCollection.iterator();
        while (it.hasNext()) {
            Sequence sequence = (Sequence) it.next();
            sequences.add(sequence);
        }*/

        return temporaryFileManager.getPathFiles();
    }
    
    
    
    
        
    
    

    public List<Sequence> transfFromClassesToTuples(ProjectRevisions newProjectRevisions, Project project, JavaConstructorService javaConstructorService) {
        List<Sequence> sequences = new LinkedList();

        HashMap<String, Sequence> hashMap = new HashMap();

        System.out.println("Criando sequencias de métodos");
        Revision rev = newProjectRevisions.getRoot();
        JavaProject ant = null;
        int k = 0;
        while (rev != null) {
            //JavaProject jp = javaProjects.get(i);
            JavaProject jp = null;
            //System.out.println("REV ID: "+rev.getId());
            jp = javaConstructorService.getProjectByRevisionAndSetRevision(project.getName(), project.getCodeDirs(), project.getPath(), rev.getId(), newProjectRevisions.getName());




            k++;

            if (ant != null) {
                System.out.println("Sequencia: " + k);
                List<JavaClass> godClassEmerge = new LinkedList();
                List<JavaClass> godClassCorrect = new LinkedList();
                getGodClassesChange(ant, jp, godClassEmerge, godClassCorrect);
                List<JavaPackage> godPackageEmerge = new LinkedList();
                List<JavaPackage> godPackageCorrect = new LinkedList();
                getGodPackageChange(ant, jp, godPackageEmerge, godPackageCorrect);
                List<JavaClass> misplacedClassEmerge = new LinkedList();
                List<JavaClass> misplacedClassCorrect = new LinkedList();
                getMisplacedClassChange(ant, jp, misplacedClassEmerge, misplacedClassCorrect);
                for (JavaAbstract javaAbstract : jp.getClasses()) {
                    JavaClass jc = (JavaClass) javaAbstract;
                    JavaClass antClass = null;

                    JavaAbstract antAbstract = ant.getClassByName(jc.getFullQualifiedName());
                    if (antAbstract != null) {
                        if (antAbstract.getClass() == JavaClass.class) {
                            antClass = (JavaClass) ant.getClassByName(jc.getFullQualifiedName());
                        }
                    }

                    if (antClass != null) {
                        //aqui começa a criação das sequencias
                        Sequence sequence = hashMap.get(jc.getFullQualifiedName());
                        if (sequence == null) {
                            sequence = new Sequence();
                            hashMap.put(jc.getFullQualifiedName(), sequence);
                        }
                        String tupleStr = "";
                        if (jc.getAttributes().size() != antClass.getAttributes().size()) {
                            if (jc.getAttributes().size() > antClass.getAttributes().size()) {
                                tupleStr = tupleStr + "number_of_attributes:+ ";
                            } else {
                                tupleStr = tupleStr + "number_of_attributes:- ";
                            }
                        }
                        if (jc.getMethods().size() != antClass.getMethods().size()) {
                            if (jc.getMethods().size() > antClass.getMethods().size()) {
                                tupleStr = tupleStr + "number_of_methods:+ ";
                            } else {
                                tupleStr = tupleStr + "number_of_methods:- ";
                            }
                        }
                        if (jc.getTotalCyclomaticComplexity() != antClass.getTotalCyclomaticComplexity()) {
                            if (jc.getTotalCyclomaticComplexity() > antClass.getTotalCyclomaticComplexity()) {
                                tupleStr = tupleStr + "total_cyclomatic_complexity:+ ";
                            } else {
                                tupleStr = tupleStr + "total_cyclomatic_complexity:- ";
                            }
                        }
                        if (jc.getImplementedInterfaces().size() != antClass.getImplementedInterfaces().size()) {
                            if (jc.getImplementedInterfaces().size() > antClass.getImplementedInterfaces().size()) {
                                tupleStr = tupleStr + "number_of_implemented_interfaces:+ ";
                            } else {
                                tupleStr = tupleStr + "number_of_implemented_interfaces:- ";
                            }
                        }
                        if (jc.getAccessToForeignDataNumber() != antClass.getAccessToForeignDataNumber()) {
                            if (jc.getAccessToForeignDataNumber() > antClass.getAccessToForeignDataNumber()) {
                                tupleStr = tupleStr + "access_to_foreign_data_number_class:+ ";
                            } else {
                                tupleStr = tupleStr + "access_to_foreign_data_number_class:- ";
                            }
                        }
                        if (jc.getClientClasses().size() != antClass.getClientClasses().size()) {
                            if (jc.getClientClasses().size() > antClass.getClientClasses().size()) {
                                tupleStr = tupleStr + "number_of_client_classes_class:+ ";
                            } else {
                                tupleStr = tupleStr + "number_of_client_classes_class:- ";
                            }
                        }
                        if (jc.getClientPackages().size() != antClass.getClientPackages().size()) {
                            if (jc.getClientPackages().size() > antClass.getClientPackages().size()) {
                                tupleStr = tupleStr + "number_of_client_packages_class:+ ";
                            } else {
                                tupleStr = tupleStr + "number_of_client_packages_class:- ";
                            }
                        }
                        if (jc.getExternalDependencyClasses().size() != antClass.getExternalDependencyClasses().size()) {
                            if (jc.getExternalDependencyClasses().size() > antClass.getExternalDependencyClasses().size()) {
                                tupleStr = tupleStr + "number_of_external_dependency_classes_class:+ ";
                            } else {
                                tupleStr = tupleStr + "number_of_external_dependency_classes_class:- ";
                            }
                        }
                        if (jc.getExternalDependencyPackages().size() != antClass.getExternalDependencyPackages().size()) {
                            if (jc.getExternalDependencyPackages().size() > antClass.getExternalDependencyPackages().size()) {
                                tupleStr = tupleStr + "number_of_external_dependency_packages_class:+ ";
                            } else {
                                tupleStr = tupleStr + "number_of_external_dependency_packages_class:- ";
                            }
                        }
                        if (jc.getInternalDependencyClasses().size() != antClass.getInternalDependencyClasses().size()) {
                            if (jc.getInternalDependencyClasses().size() > antClass.getInternalDependencyClasses().size()) {
                                tupleStr = tupleStr + "number_of_internal_dependency_classes_class:+ ";
                            } else {
                                tupleStr = tupleStr + "number_of_internal_dependency_classes_class:- ";
                            }
                        }
                        if (jc.getNumberOfDirectConnections() != antClass.getNumberOfDirectConnections()) {
                            if (jc.getNumberOfDirectConnections() > antClass.getNumberOfDirectConnections()) {
                                tupleStr = tupleStr + "number_of_direct_connections:+ ";
                            } else {
                                tupleStr = tupleStr + "number_of_direct_connections:- ";
                            }
                        }
                        if (jc.getintraPackageDependentClass().size() != antClass.getintraPackageDependentClass().size()) {
                            if (jc.getintraPackageDependentClass().size() > antClass.getintraPackageDependentClass().size()) {
                                tupleStr = tupleStr + "number_of_intra_package_dependent_class:+ ";
                            } else {
                                tupleStr = tupleStr + "number_of_intra_package_dependent_class:- ";
                            }
                        }






                        //god class
                        boolean changeSameClass = false;
                        boolean changeOtherClassSamePackage = false;
                        boolean changeOtherClassOtherPackage = false;
                        for (JavaClass javaClass : godClassEmerge) {
                            if (javaClass.getFullQualifiedName().equals(jc.getFullQualifiedName())) {
                                changeSameClass = true;
                            } else {
                                if (javaClass.getJavaPackage().getName().equals(jc.getJavaPackage().getName())) {
                                    changeOtherClassSamePackage = true;
                                } else {
                                    changeOtherClassOtherPackage = true;
                                }
                            }
                        }
                        if (changeSameClass) {
                            tupleStr = tupleStr + "god_class_in_this_class_emerge:+ ";
                        }
                        if (changeOtherClassSamePackage) {
                            tupleStr = tupleStr + "god_class_in_another_class_in_same_package_emerge:+ ";
                        }
                        if (changeOtherClassOtherPackage) {
                            tupleStr = tupleStr + "god_class_in_another_class_in_another_package_emerge:+ ";
                        }
                        changeSameClass = false;
                        changeOtherClassSamePackage = false;
                        changeOtherClassOtherPackage = false;
                        for (JavaClass javaClass : godClassCorrect) {
                            if (javaClass.getFullQualifiedName().equals(jc.getFullQualifiedName())) {
                                changeSameClass = true;

                            } else {
                                if (javaClass.getJavaPackage().getName().equals(jc.getJavaPackage().getName())) {
                                    changeOtherClassSamePackage = true;
                                } else {
                                    changeOtherClassOtherPackage = true;
                                }
                            }
                        }
                        if (changeSameClass) {
                            tupleStr = tupleStr + "god_class_in_this_class_correct:+ ";
                        }
                        if (changeOtherClassSamePackage) {
                            tupleStr = tupleStr + "god_class_in_another_class_in_same_package_correct:+ ";
                        }
                        if (changeOtherClassOtherPackage) {
                            tupleStr = tupleStr + "god_class_in_another_class_in_another_package_correct:+ ";
                        }


                        // misplaced class
                        changeSameClass = false;
                        changeOtherClassSamePackage = false;
                        changeOtherClassOtherPackage = false;
                        for (JavaClass javaClass : misplacedClassEmerge) {
                            if (javaClass.getFullQualifiedName().equals(jc.getFullQualifiedName())) {
                                changeSameClass = true;
                            } else {
                                if (javaClass.getJavaPackage().getName().equals(jc.getJavaPackage().getName())) {
                                    changeOtherClassSamePackage = true;
                                } else {
                                    changeOtherClassOtherPackage = true;
                                }
                            }
                        }
                        if (changeSameClass) {
                            tupleStr = tupleStr + "misplaced_class_in_this_class_emerge:+ ";
                        }
                        if (changeOtherClassSamePackage) {
                            tupleStr = tupleStr + "misplaced_class_in_another_class_in_same_package_emerge:+ ";
                        }
                        if (changeOtherClassOtherPackage) {
                            tupleStr = tupleStr + "misplaced_class_in_another_class_in_another_package_emerge:+ ";
                        }
                        changeSameClass = false;
                        changeOtherClassSamePackage = false;
                        changeOtherClassOtherPackage = false;
                        for (JavaClass javaClass : misplacedClassCorrect) {
                            if (javaClass.getFullQualifiedName().equals(jc.getFullQualifiedName())) {
                                changeSameClass = true;

                            } else {
                                if (javaClass.getJavaPackage().getName().equals(jc.getJavaPackage().getName())) {
                                    changeOtherClassSamePackage = true;
                                } else {
                                    changeOtherClassOtherPackage = true;
                                }
                            }
                        }
                        if (changeSameClass) {
                            tupleStr = tupleStr + "misplaced_class_in_this_class_correct:+ ";
                        }
                        if (changeOtherClassSamePackage) {
                            tupleStr = tupleStr + "misplaced_class_in_another_class_in_same_package_correct:+ ";
                        }
                        if (changeOtherClassOtherPackage) {
                            tupleStr = tupleStr + "misplaced_class_in_another_class_in_another_package_correct:+ ";
                        }


                        //god package
                        boolean changeSamePackage = false;
                        boolean changeOtherPackage = false;
                        for (JavaPackage javaPackage : godPackageEmerge) {
                            if (javaPackage.getName().equals(jc.getJavaPackage().getName())) {
                                changeSamePackage = true;
                                if (changeOtherPackage) {
                                    break;
                                }
                            } else {
                                changeOtherPackage = true;
                                if (changeSamePackage) {
                                    break;
                                }
                            }
                        }
                        if (changeSamePackage) {
                            tupleStr = tupleStr + "god_package_in_same_package_emerge:+ ";
                        }
                        if (changeOtherPackage) {
                            tupleStr = tupleStr + "god_package_in_another_package_emerge:+ ";
                        }
                        changeSamePackage = false;
                        changeOtherPackage = false;
                        for (JavaPackage javaPackage : godPackageCorrect) {
                            if (javaPackage.getName().equals(jc.getJavaPackage().getName())) {
                                changeSamePackage = true;
                                if (changeOtherPackage) {
                                    break;
                                }
                            } else {
                                changeOtherPackage = true;
                                if (changeSamePackage) {
                                    break;
                                }
                            }
                        }
                        if (changeSamePackage) {
                            tupleStr = tupleStr + "god_package_in_same_package_correct:+ ";
                        }
                        if (changeOtherPackage) {
                            tupleStr = tupleStr + "god_package_in_another_package_correct:+ ";
                        }



                        if (!tupleStr.equals("")) {
                            tupleStr = tupleStr.substring(0, tupleStr.length() - 1);
                            sequence.addItem(tupleStr);
                        }



                    }
                }


            }
            ant = jp;
            if (rev.getNext().size() == 0) {
                rev = null;
            } else {
                rev = rev.getNext().get(0);
            }

        }

        Collection<Sequence> sequenceCollection = hashMap.values();
        Iterator it = sequenceCollection.iterator();
        while (it.hasNext()) {
            Sequence sequence = (Sequence) it.next();
            sequences.add(sequence);
        }

        return sequences;
    }
    
    
    public boolean isAArchitectureFlaw(String text){
        boolean flag = false;
        
        if (text.equals("feature_envy_in_this_method_emerge:+")) {
            flag = true;
        } else if (text.equals("feature_envy_in_another_method_in_same_class_emerge:+")) {
            flag = true;
        } else if (text.equals("feature_envy_in_this_method_correct:+")) {
            flag = true;
        } else if (text.equals("feature_envy_in_another_method_in_same_class_correct:+")) {
            flag = true;
        } else if (text.equals("shotgun_surgery_in_this_method_emerge:+")) {
            flag = true;
        } else if (text.equals("shotgun_surgery_in_another_method_in_same_class_emerge:+")) {
            flag = true;
        } else if (text.equals("shotgun_surgery_in_this_method_correct:+")) {
            flag = true;
        } else if (text.equals("shotgun_surgery_in_another_method_in_same_class_correct:+")) {
            flag = true;
        } else if (text.equals("god_method_in_this_method_emerge:+")) {
            flag = true;
        } else if (text.equals("god_method_in_another_method_in_same_class_emerge:+")) {
            flag = true;
        } else if (text.equals("god_method_in_this_method_correct:+")) {
            flag = true;
        } else if (text.equals("god_method_in_another_method_in_same_class_correct:+")) {
            flag = true;
        } else if (text.equals("god_class_in_this_class_emerge:+")) {
            flag = true;
        } else if (text.equals("god_class_in_another_class_in_same_package_emerge:+")) {
            flag = true;
        } else if (text.equals("god_class_in_another_class_in_another_package_emerge:+")) {
            flag = true;
        } else if (text.equals("god_class_in_this_class_correct:+")) {
            flag = true;
        } else if (text.equals("god_class_in_another_class_in_same_package_correct:+")) {
            flag = true;
        } else if (text.equals("god_class_in_another_class_in_another_package_correct:+")) {
            flag = true;
        } else if (text.equals("misplaced_class_in_this_class_emerge:+")) {
            flag = true;
        } else if (text.equals("misplaced_class_in_another_class_in_same_package_emerge:+")) {
            flag = true;
        } else if (text.equals("misplaced_class_in_another_class_in_another_package_emerge:+")) {
            flag = true;
        } else if (text.equals("misplaced_class_in_this_class_correct:+")) {
            flag = true;
        } else if (text.equals("misplaced_class_in_another_class_in_same_package_correct:+")) {
            flag = true;
        } else if (text.equals("misplaced_class_in_another_class_in_another_package_correct:+")) {
            flag = true;
        } else if (text.equals("god_package_in_same_package_emerge:+")) {
            flag = true;
        } else if (text.equals("god_package_in_another_package_emerge:+")) {
            flag = true;
        } else if (text.equals("god_package_in_same_package_correct:+")) {
            flag = true;
        } else if (text.equals("god_package_in_another_package_correct:+")) {
            flag = true;
        }
        
        return flag;
    }

    public int changeTextToNumber(String text) throws Exception {
        if (text.equals("number_of_lines:+")) {
            return 1;
        } else if (text.equals("number_of_lines:-")) {
            return 2;
        } else if (text.equals("size_in_chars:+")) {
            return 3;
        } else if (text.equals("size_in_chars:-")) {
            return 4;
        } else if (text.equals("number_of_local_variables:+")) {
            return 5;
        } else if (text.equals("number_of_local_variables:-")) {
            return 6;
        } else if (text.equals("cyclomatic_complexity:+")) {
            return 7;
        } else if (text.equals("cyclomatic_complexity:-")) {
            return 8;
        } else if (text.equals("number_of_external_method_invocations:+")) {
            return 9;
        } else if (text.equals("number_of_external_method_invocations:-")) {
            return 10;
        } else if (text.equals("access_to_foreign_data_number:+")) {
            return 11;
        } else if (text.equals("access_to_foreign_data_number:-")) {
            return 12;
        } else if (text.equals("access_to_local_data_number:+")) {
            return 13;
        } else if (text.equals("access_to_local_data_number:-")) {
            return 14;
        } else if (text.equals("change_classes_metric:+")) {
            return 15;
        } else if (text.equals("change_classes_metric:-")) {
            return 16;
        } else if (text.equals("number_of_changing_methods:+")) {
            return 17;
        } else if (text.equals("number_of_changing_methods:-")) {
            return 18;
        } else if (text.equals("changing_methods_metric:+")) {
            return 19;
        } else if (text.equals("changing_methods_metric:-")) {
            return 20;
        } else if (text.equals("foreign_data_provider_number:+")) {
            return 21;
        } else if (text.equals("foreign_data_provider_number:-")) {
            return 22;
        } else if (text.equals("number_of_internal_method_invocations:+")) {
            return 23;
        } else if (text.equals("number_of_internal_method_invocations:-")) {
            return 24;
        } else if (text.equals("number_of_external_access_attribute:+")) {
            return 25;
        } else if (text.equals("number_of_external_access_attribute:-")) {
            return 26;
        } else if (text.equals("change_return_type:+")) {
            return 27;
        } else if (text.equals("change_to_abstract:+")) {
            return 28;
        } else if (text.equals("change_to_not_abstract:+")) {
            return 29;
        } else if (text.equals("change_to_public:+")) {
            return 30;
        } else if (text.equals("change_to_private:+")) {
            return 31;
        } else if (text.equals("change_to_protected:+")) {
            return 32;
        } else if (text.equals("change_to_static:+")) {
            return 33;
        } else if (text.equals("change_to_not_static:+")) {
            return 34;
        } else if (text.equals("change_to_final:+")) {
            return 35;
        } else if (text.equals("change_to_not_final:+")) {
            return 36;
        } else if (text.equals("change_to_synchronized:+")) {
            return 37;
        } else if (text.equals("change_to_not_synchronized:+")) {
            return 38;
        } else if (text.equals("change_to_accessor_method:+")) {
            return 39;
        } else if (text.equals("change_to_not_accessor_method:+")) {
            return 40;
        } else if (text.equals("change_to_change_internal_state:+")) {
            return 41;
        } else if (text.equals("change_to_not_change_internal_state:+")) {
            return 42;
        } else if (text.equals("change_to_change_internal_state_by_method_invocation:+")) {
            return 43;
        } else if (text.equals("change_to_not_change_internal_state_by_method_invocation:+")) {
            return 44;
        } else if (text.equals("feature_envy_in_this_method_emerge:+")) {
            return 45;
        } else if (text.equals("feature_envy_in_another_method_in_same_class_emerge:+")) {
            return 46;
        } else if (text.equals("feature_envy_in_this_method_correct:+")) {
            return 47;
        } else if (text.equals("feature_envy_in_another_method_in_same_class_correct:+")) {
            return 48;
        } else if (text.equals("shotgun_surgery_in_this_method_emerge:+")) {
            return 49;
        } else if (text.equals("shotgun_surgery_in_another_method_in_same_class_emerge:+")) {
            return 50;
        } else if (text.equals("shotgun_surgery_in_this_method_correct:+")) {
            return 51;
        } else if (text.equals("shotgun_surgery_in_another_method_in_same_class_correct:+")) {
            return 52;
        } else if (text.equals("god_method_in_this_method_emerge:+")) {
            return 53;
        } else if (text.equals("god_method_in_another_method_in_same_class_emerge:+")) {
            return 54;
        } else if (text.equals("god_method_in_this_method_correct:+")) {
            return 55;
        } else if (text.equals("god_method_in_another_method_in_same_class_correct:+")) {
            return 56;
        } else if (text.equals("god_class_in_this_class_emerge:+")) {
            return 57;
        } else if (text.equals("god_class_in_another_class_in_same_package_emerge:+")) {
            return 58;
        } else if (text.equals("god_class_in_another_class_in_another_package_emerge:+")) {
            return 59;
        } else if (text.equals("god_class_in_this_class_correct:+")) {
            return 60;
        } else if (text.equals("god_class_in_another_class_in_same_package_correct:+")) {
            return 61;
        } else if (text.equals("god_class_in_another_class_in_another_package_correct:+")) {
            return 62;
        } else if (text.equals("misplaced_class_in_this_class_emerge:+")) {
            return 63;
        } else if (text.equals("misplaced_class_in_another_class_in_same_package_emerge:+")) {
            return 64;
        } else if (text.equals("misplaced_class_in_another_class_in_another_package_emerge:+")) {
            return 65;
        } else if (text.equals("misplaced_class_in_this_class_correct:+")) {
            return 66;
        } else if (text.equals("misplaced_class_in_another_class_in_same_package_correct:+")) {
            return 67;
        } else if (text.equals("misplaced_class_in_another_class_in_another_package_correct:+")) {
            return 68;
        } else if (text.equals("god_package_in_same_package_emerge:+")) {
            return 69;
        } else if (text.equals("god_package_in_another_package_emerge:+")) {
            return 70;
        } else if (text.equals("god_package_in_same_package_correct:+")) {
            return 71;
        } else if (text.equals("god_package_in_another_package_correct:+")) {
            return 72;
        } else if (text.equals("number_of_attributes_in_same_class:+")) {
            return 72;
        } else if (text.equals("number_of_attributes_in_same_class:-")) {
            return 72;
        } else if (text.equals("number_of_methods_in_same_class:+")) {
            return 72;
        } else if (text.equals("number_of_methods_in_same_class:-")) {
            return 72;
        } else if (text.equals("total_cyclomatic_complexity_in_same_class:+")) {
            return 72;
        } else if (text.equals("total_cyclomatic_complexity_in_same_class:-")) {
            return 72;
        } else if (text.equals("number_of_implemented_interfaces_in_same_class:+")) {
            return 72;
        } else if (text.equals("number_of_implemented_interfaces_in_same_class:-")) {
            return 72;
        } else if (text.equals("access_to_foreign_data_number_in_same_class:+")) {
            return 72;
        } else if (text.equals("access_to_foreign_data_number_in_same_class:-")) {
            return 72;
        } else if (text.equals("number_of_client_classes_in_same_class:+")) {
            return 72;
        } else if (text.equals("number_of_client_classes_in_same_class:-")) {
            return 72;
        } else if (text.equals("number_of_client_packages_in_same_class:+")) {
            return 72;
        } else if (text.equals("number_of_client_packages_in_same_class:-")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else if (text.equals("number_of_attributes:+")) {
            return 72;
        } else {
            throw new Exception("");
        }



    }

    public String changeNumberToText(int number) throws Exception {

        if (number == 1) {
            return "number_of_lines:+";
        } else if (number == 2) {
            return "number_of_lines:-";
        } else if (number == 3) {
            return "size_in_chars:+";
        } else if (number == 4) {
            return "size_in_chars:-";
        } else if (number == 5) {
            return "number_of_local_variables:+";
        } else if (number == 6) {
            return "number_of_local_variables:-";
        } else if (number == 7) {
            return "cyclomatic_complexity:+";
        } else if (number == 8) {
            return "cyclomatic_complexity:-";
        } else if (number == 9) {
            return "number_of_method_invocations:+";
        } else if (number == 10) {
            return "number_of_method_invocations:-";
        } else if (number == 11) {
            return "access_to_foreign_data_number:+";
        } else if (number == 12) {
            return "access_to_foreign_data_number:-";
        } else if (number == 13) {
            return "access_to_local_data_number:+";
        } else if (number == 14) {
            return "access_to_local_data_number:-";
        } else if (number == 15) {
            return "change_classes_metric:+";
        } else if (number == 16) {
            return "change_classes_metric:-";
        } else if (number == 17) {
            return "number_of_changing_methods:+";
        } else if (number == 18) {
            return "number_of_changing_methods:-";
        } else if (number == 19) {
            return "changing_methods_metric:+";
        } else if (number == 20) {
            return "changing_methods_metric:-";
        } else if (number == 21) {
            return "foreign_data_provider_number:+";
        } else if (number == 22) {
            return "foreign_data_provider_number:-";
        } else if (number == 23) {
            return "number_of_internal_method_invocations:+";
        } else if (number == 24) {
            return "number_of_internal_method_invocations:-";
        } else if (number == 25) {
            return "number_of_external_access_attribute:+";
        } else if (number == 26) {
            return "number_of_external_access_attribute:-";
        } else if (number == 27) {
            return "change_return_type:+";
        } else if (number == 28) {
            return "change_to_abstract:+";
        } else if (number == 29) {
            return "change_to_not_abstract:+";
        } else if (number == 30) {
            return "change_to_public:+";
        } else if (number == 31) {
            return "change_to_private:+";
        } else if (number == 32) {
            return "change_to_protected:+";
        } else if (number == 33) {
            return "change_to_static:+";
        } else if (number == 34) {
            return "change_to_not_static:+";
        } else if (number == 35) {
            return "change_to_final:+";
        } else if (number == 36) {
            return "change_to_not_final:+";
        } else if (number == 37) {
            return "change_to_synchronized:+";
        } else if (number == 38) {
            return "change_to_not_synchronized:+";
        } else if (number == 39) {
            return "change_to_accessor_method:+";
        } else if (number == 40) {
            return "change_to_not_accessor_method:+";
        } else if (number == 41) {
            return "change_to_change_internal_state:+";
        } else if (number == 42) {
            return "change_to_not_change_internal_state:+";
        } else if (number == 43) {
            return "change_to_change_internal_state_by_method_invocation:+";
        } else if (number == 44) {
            return "change_to_not_change_internal_state_by_method_invocation:+";
        } else if (number == 45) {
            return "feature_envy_in_this_method_emerge:+";
        } else if (number == 46) {
            return "feature_envy_in_another_method_in_same_class_emerge:+";
        } else if (number == 47) {
            return "feature_envy_in_this_method_correct:+";
        } else if (number == 48) {
            return "feature_envy_in_another_method_in_same_class_correct:+";
        } else if (number == 49) {
            return "shotgun_surgery_in_this_method_emerge:+";
        } else if (number == 50) {
            return "shotgun_surgery_in_another_method_in_same_class_emerge:+";
        } else if (number == 51) {
            return "shotgun_surgery_in_this_method_correct:+";
        } else if (number == 52) {
            return "shotgun_surgery_in_another_method_in_same_class_correct:+";
        } else if (number == 53) {
            return "god_method_in_this_method_emerge:+";
        } else if (number == 54) {
            return "god_method_in_another_method_in_same_class_emerge:+";
        } else if (number == 55) {
            return "god_method_in_this_method_correct:+";
        } else if (number == 56) {
            return "god_method_in_another_method_in_same_class_correct:+";
        } else if (number == 57) {
            return "god_class_in_this_class_emerge:+";
        } else if (number == 58) {
            return "god_class_in_another_class_in_same_package_emerge:+";
        } else if (number == 59) {
            return "god_class_in_another_class_in_another_package_emerge:+";
        } else if (number == 60) {
            return "god_class_in_this_class_correct:+";
        } else if (number == 61) {
            return "god_class_in_another_class_in_same_package_correct:+";
        } else if (number == 62) {
            return "god_class_in_another_class_in_another_package_correct:+";
        } else if (number == 63) {
            return "misplaced_class_in_this_class_emerge:+";
        } else if (number == 64) {
            return "misplaced_class_in_another_class_in_same_package_emerge:+";
        } else if (number == 65) {
            return "misplaced_class_in_another_class_in_another_package_emerge:+";
        } else if (number == 66) {
            return "misplaced_class_in_this_class_correct:+";
        } else if (number == 67) {
            return "misplaced_class_in_another_class_in_same_package_correct:+";
        } else if (number == 68) {
            return "misplaced_class_in_another_class_in_another_package_correct:+";
        } else if (number == 69) {
            return "god_package_in_same_package_emerge:+";
        } else if (number == 70) {
            return "god_package_in_another_package_emerge:+";
        } else if (number == 71) {
            return "god_package_in_same_package_correct:+";
        } else if (number == 72) {
            return "god_package_in_another_package_correct:+";
        } else {
            throw new Exception("");
        }
    }

    private void getFeatureEnvyChange(JavaClass antClass, JavaClass jc, List<JavaMethod> emergeList, List<JavaMethod> correctList) {
        List<JavaMethod> antList = getFeatureEnvy(antClass);
        List<JavaMethod> nowList = getFeatureEnvy(jc);
        for (JavaMethod jm : nowList) {
            boolean encontrou = false;
            for (JavaMethod aux : antList) {
                if (jm.getMethodSignature().equals(aux.getMethodSignature())) {
                    encontrou = true;
                    break;
                }
            }
            if (!encontrou) {
                emergeList.add(jm);
            }
        }
        for (JavaMethod jm : antList) {
            boolean encontrou = false;
            for (JavaMethod aux : nowList) {
                if (jm.getMethodSignature().equals(aux.getMethodSignature())) {
                    encontrou = true;
                    break;
                }
            }
            if (!encontrou) {
                correctList.add(jm);
            }
        }
    }

    private void getShotgunSurgeryChange(JavaClass antClass, JavaClass jc, List<JavaMethod> emergeList, List<JavaMethod> correctList) {
        List<JavaMethod> antList = getShotgunSurgery(antClass);
        List<JavaMethod> nowList = getShotgunSurgery(jc);
        for (JavaMethod jm : nowList) {
            boolean encontrou = false;
            for (JavaMethod aux : antList) {
                if (jm.getMethodSignature().equals(aux.getMethodSignature())) {
                    encontrou = true;
                    break;
                }
            }
            if (!encontrou) {
                emergeList.add(jm);
            }
        }
        for (JavaMethod jm : antList) {
            boolean encontrou = false;
            for (JavaMethod aux : nowList) {
                if (jm.getMethodSignature().equals(aux.getMethodSignature())) {
                    encontrou = true;
                    break;
                }
            }
            if (!encontrou) {
                correctList.add(jm);
            }
        }
    }

    private void getGodMethodChange(JavaClass antClass, JavaClass jc, List<JavaMethod> emergeList, List<JavaMethod> correctList) {
        List<JavaMethod> antList = getGodMethod(antClass);
        List<JavaMethod> nowList = getGodMethod(jc);
        for (JavaMethod jm : nowList) {
            boolean encontrou = false;
            for (JavaMethod aux : antList) {
                if (jm.getMethodSignature().equals(aux.getMethodSignature())) {
                    encontrou = true;
                    break;
                }
            }
            if (!encontrou) {
                emergeList.add(jm);
            }
        }
        for (JavaMethod jm : antList) {
            boolean encontrou = false;
            for (JavaMethod aux : nowList) {
                if (jm.getMethodSignature().equals(aux.getMethodSignature())) {
                    encontrou = true;
                    break;
                }
            }
            if (!encontrou) {
                correctList.add(jm);
            }
        }
    }

    private void getGodClassesChange(JavaProject antProject, JavaProject jp, List<JavaClass> emergeList, List<JavaClass> correctList) {
        List<JavaClass> antList = getGodClass(antProject);
        List<JavaClass> nowList = getGodClass(jp);
        for (JavaClass jc : nowList) {
            boolean encontrou = false;
            for (JavaClass aux : antList) {
                if (jc.getFullQualifiedName().equals(aux.getFullQualifiedName())) {
                    encontrou = true;
                    break;
                }
            }
            if (!encontrou) {
                emergeList.add(jc);
            }
        }
        for (JavaClass jc : antList) {
            boolean encontrou = false;
            for (JavaClass aux : nowList) {
                if (jc.getFullQualifiedName().equals(aux.getFullQualifiedName())) {
                    encontrou = true;
                    break;
                }
            }
            if (!encontrou) {
                correctList.add(jc);
            }
        }
    }

    private void getGodPackageChange(JavaProject antProject, JavaProject jp, List<JavaPackage> emergeList, List<JavaPackage> correctList) {
        List<JavaPackage> antList = getGodPackage(antProject);
        List<JavaPackage> nowList = getGodPackage(jp);
        for (JavaPackage jc : nowList) {
            boolean encontrou = false;
            for (JavaPackage aux : antList) {
                if (jc.getName().equals(aux.getName())) {
                    encontrou = true;
                    break;
                }
            }
            if (!encontrou) {
                emergeList.add(jc);
            }
        }
        for (JavaPackage jc : antList) {
            boolean encontrou = false;
            for (JavaPackage aux : nowList) {
                if (jc.getName().equals(aux.getName())) {
                    encontrou = true;
                    break;
                }
            }
            if (!encontrou) {
                correctList.add(jc);
            }
        }
    }

    private void getMisplacedClassChange(JavaProject antProject, JavaProject jp, List<JavaClass> emergeList, List<JavaClass> correctList) {
        List<JavaClass> antList = getMisplacedClass(antProject);
        List<JavaClass> nowList = getMisplacedClass(jp);
        for (JavaClass jc : nowList) {
            boolean encontrou = false;
            for (JavaClass aux : antList) {
                if (jc.getFullQualifiedName().equals(aux.getFullQualifiedName())) {
                    encontrou = true;
                    break;
                }
            }
            if (!encontrou) {
                emergeList.add(jc);
            }
        }
        for (JavaClass jc : antList) {
            boolean encontrou = false;
            for (JavaClass aux : nowList) {
                if (jc.getFullQualifiedName().equals(aux.getFullQualifiedName())) {
                    encontrou = true;
                    break;
                }
            }
            if (!encontrou) {
                correctList.add(jc);
            }
        }
    }

    public List<JavaMethod> getFeatureEnvy(JavaClass jc) {
        //feature envy
        List<JavaMethod> featureEnvyList = new LinkedList();
        if (!jc.getMethods().isEmpty()) {
            List<JavaMethod> topValuesMethods = new LinkedList();
            List<JavaMethod> auxList = new LinkedList();

            auxList.add(jc.getMethods().get(0));
            for (int i = 1; i < jc.getMethods().size(); i++) {
                JavaMethod javaMethod = jc.getMethods().get(i);
                boolean inseriu = false;
                for (int j = 0; j < auxList.size(); j++) {
                    JavaMethod jm2 = auxList.get(j);
                    if (javaMethod.getAccessToForeignDataNumber() > jm2.getAccessToForeignDataNumber()) {
                        auxList.add(j, javaMethod);
                        inseriu = true;
                        break;
                    }
                }
                if (!inseriu) {
                    auxList.add(javaMethod);
                }
            }
            int topNumber = jc.getMethods().size() / 10;
            if (topNumber * 10 != jc.getMethods().size()) {
                topNumber++;
            }
            for (int i = 0; i < topNumber; i++) {
                topValuesMethods.add(auxList.get(i));
            }

            for (JavaMethod javaMethod : topValuesMethods) {
                if ((javaMethod.getAccessToForeignDataNumber() >= 4)
                        && (javaMethod.getAccessToLocalDataNumber() <= 3)
                        && (javaMethod.getForeignDataProviderNumber() <= 3)) {
                    featureEnvyList.add(javaMethod);
                }
            }

        }

        return featureEnvyList;
    }

    public List<JavaMethod> getShotgunSurgery(JavaClass jc) {
        //feature envy
        List<JavaMethod> shotgunList = new LinkedList();
        if (!jc.getMethods().isEmpty()) {

            for (JavaMethod jm : jc.getMethods()) {
                if (jm.getChangingMethodsMetric() > 7 && jm.getChangingClassesMetric() > 5) {
                    shotgunList.add(jm);
                }
            }

        }

        return shotgunList;
    }

    public List<JavaMethod> getGodMethod(JavaClass jc) {
        //feature envy
        List<JavaMethod> godMethodList = new LinkedList();
        if (!jc.getMethods().isEmpty()) {
            List<JavaMethod> topValuesMethods = new LinkedList();
            List<JavaMethod> auxList = new LinkedList();
            auxList.add(jc.getMethods().get(0));
            for (int i = 1; i < jc.getMethods().size(); i++) {
                JavaMethod javaMethod = jc.getMethods().get(i);
                boolean inseriu = false;
                for (int j = 0; j < auxList.size(); j++) {
                    JavaMethod jm2 = auxList.get(j);
                    if (javaMethod.getNumberOfLines() > jm2.getNumberOfLines()) {
                        auxList.add(j, javaMethod);
                        inseriu = true;
                        break;
                    }
                }
                if (!inseriu) {
                    auxList.add(javaMethod);
                }
            }
            int topNumber = jc.getMethods().size() / 5;
            if (topNumber * 5 != jc.getMethods().size()) {
                topNumber++;
            }
            for (int i = 0; i < topNumber; i++) {
                topValuesMethods.add(auxList.get(i));
            }

            for (JavaMethod javaMethod : topValuesMethods) {
                if ((javaMethod.getNumberOfLines() >= 70)
                        && (javaMethod.getParameters().size() >= 4 || javaMethod.getNumberOfLocalVariables() >= 4)
                        && (javaMethod.getCyclomaticComplexity() >= 4)) {
                    godMethodList.add(javaMethod);
                }
            }



        }
        return godMethodList;
    }

    public List<JavaClass> getGodClass(JavaProject jp) {
        List<JavaClass> godClassList = new LinkedList();
        if (!jp.getClasses().isEmpty()) {
            List<JavaClass> topValuesClasses = new LinkedList();
            List<JavaClass> auxList = new LinkedList();

            auxList.add((JavaClass) jp.getClasses().get(0));
            for (int i = 1; i < jp.getClasses().size(); i++) {
                JavaClass javaClass = (JavaClass) jp.getClasses().get(i);
                boolean inseriu = false;
                for (int j = 0; j < auxList.size(); j++) {
                    JavaClass jc2 = auxList.get(j);
                    if (javaClass.getAccessToForeignDataNumber() > jc2.getAccessToForeignDataNumber()) {
                        auxList.add(j, javaClass);
                        inseriu = true;
                        break;
                    }
                }
                if (!inseriu) {
                    auxList.add(javaClass);
                }
            }
            int topNumber = jp.getClasses().size() / 5;
            if (topNumber * 5 != jp.getClasses().size()) {
                topNumber++;
            }
            for (int i = 0; i < topNumber; i++) {
                topValuesClasses.add(auxList.get(i));
            }

            for (JavaClass javaClass : topValuesClasses) {
                double tcc = javaClass.getNumberOfDirectConnections();
                int n = javaClass.getMethods().size();
                tcc = tcc / ((n * (n - 1)) / 2);
                if ((javaClass.getAccessToForeignDataNumber() >= 4)
                        && (javaClass.getTotalCyclomaticComplexity() >= 20)
                        && (tcc <= 0.33)) {
                    godClassList.add(javaClass);
                }
            }




        }
        return godClassList;
    }

    public List<JavaPackage> getGodPackage(JavaProject jp) {
        List<JavaPackage> godPackageList = new LinkedList();
        if (!jp.getPackages().isEmpty()) {
            List<JavaPackage> topValuesPackages = new LinkedList();
            List<JavaPackage> auxList = new LinkedList();

            auxList.add(jp.getPackages().get(0));
            for (int i = 1; i < jp.getPackages().size(); i++) {
                JavaPackage javaPackage = jp.getPackages().get(i);
                boolean inseriu = false;
                for (int j = 0; j < auxList.size(); j++) {
                    JavaPackage jp2 = auxList.get(j);
                    if (javaPackage.getOnlyClasses().size() > jp2.getOnlyClasses().size()) {
                        auxList.add(j, javaPackage);
                        inseriu = true;
                        break;
                    }
                }
                if (!inseriu) {
                    auxList.add(javaPackage);
                }
            }
            int topNumber = jp.getPackages().size() / 4;
            if (topNumber * 4 != jp.getPackages().size()) {
                topNumber++;
            }
            for (int i = 0; i < topNumber; i++) {
                topValuesPackages.add(auxList.get(i));
            }

            for (JavaPackage javaPackage : topValuesPackages) {
                double packageCohesion = javaPackage.getPackageCohesion();

                if ((javaPackage.getOnlyClasses().size() >= 20)
                        && (javaPackage.getClientClasses().size() >= 20)
                        && (javaPackage.getClientPackages().size() >= 3)) {
                    godPackageList.add(javaPackage);
                }
            }




        }
        return godPackageList;
    }

    public List<JavaClass> getMisplacedClass(JavaProject jp) {
        List<JavaClass> misplacedClassList = new LinkedList();
        if (!jp.getClasses().isEmpty()) {
            List<JavaClass> topValuesClasses = new LinkedList();
            List<JavaClass> auxList = new LinkedList();
            auxList.add((JavaClass) jp.getClasses().get(0));
            for (int i = 1; i < jp.getClasses().size(); i++) {
                JavaClass javaClass = (JavaClass) jp.getClasses().get(i);
                boolean inseriu = false;
                for (int j = 0; j < auxList.size(); j++) {
                    JavaClass jc2 = auxList.get(j);
                    if (javaClass.getExternalDependencyClasses().size() > jc2.getExternalDependencyClasses().size()) {
                        auxList.add(j, javaClass);
                        inseriu = true;
                        break;
                    }
                }
                if (!inseriu) {
                    auxList.add(javaClass);
                }
            }
            int topNumber = jp.getClasses().size() / 4;
            if (topNumber * 4 != jp.getClasses().size()) {
                topNumber++;
            }
            for (int i = 0; i < topNumber; i++) {
                topValuesClasses.add(auxList.get(i));
            }

            for (JavaClass javaClass : topValuesClasses) {
                double classLocality = javaClass.getInternalDependencyClasses().size();
                classLocality = classLocality / (javaClass.getInternalDependencyClasses().size() + javaClass.getExternalDependencyClasses().size());
                if ((javaClass.getExternalDependencyClasses().size() >= 6)
                        && (javaClass.getExternalDependencyPackages().size() <= 3)
                        && (classLocality <= 0.33)) {
                    misplacedClassList.add(javaClass);
                }
            }




        }
        return misplacedClassList;
    }
}
