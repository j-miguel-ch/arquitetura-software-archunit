package br.edu.fas;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;

import org.junit.Test;

import br.edu.fas.persistence.Dao;
import br.edu.fas.service.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

public class FooArchitectTest {
    JavaClasses importedClasses = new ClassFileImporter().importPackages("br.edu.fas");

    @Test
    public void verificarDependenciasParaCamadaPersistencia() {

        ArchRule rule = classes()
        .that().resideInAPackage("..persistence..")
        .should().onlyHaveDependentClassesThat().resideInAnyPackage("..persistence..", "..service..");    

        rule.check(importedClasses);

    }

    @Test
    public void verificarDependenciasDaCamadaPersistencia() {

        ArchRule rule = noClasses()
        .that().resideInAPackage("..persistence..")
        .should().dependOnClassesThat().resideInAnyPackage("..service..");

        rule.check(importedClasses);

    }

    @Test
    public void verificarNomesClassesCamadaPersistencia() {

        ArchRule rule = classes()
        .that().haveSimpleNameEndingWith("Dao")
        .should().resideInAPackage("..persistence..");

        rule.check(importedClasses);

    }

    @Test
    public void verificarImplementacaoInterfaceDao() {

        ArchRule rule = classes()
        .that().implement(Dao.class)
        .should().haveSimpleNameEndingWith("Dao");

        rule.check(importedClasses);

    }
    
    @Test
    public void verificarDependenciasCiclicas() {

        ArchRule rule = slices()
        .matching("br.edu.fas.(*)..").should().beFreeOfCycles();

        rule.check(importedClasses);

    }

    @Test
    public void verificarViolacaoCamadas() {

        ArchRule rule = layeredArchitecture()
        .layer("Service").definedBy("..service..")
        .layer("Persistence").definedBy("..persistence..")
    
        .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service");

        rule.check(importedClasses);

    }

    //teste novo implementado, análogo ao de persistencia (serviço)
    @Test
    public void verificarDependenciasParaCamadaServico() {

        ArchRule rule = classes()
        .that().resideInAPackage("..service..")
        .should().onlyHaveDependentClassesThat().resideInAnyPackage("..persistence..", "..service..");    

        rule.check(importedClasses);

    }

    //teste novo implementado, análogo ao de persistencia (serviço)
    @Test
    public void verificarDependenciasDaCamadaServico(){
        ArchRule rule = noClasses()
        .that().resideInAPackage("..service..")
        .should().dependOnClassesThat().resideInAnyPackage("..persistence..");

        rule.check(importedClasses);

    }

    //teste novo implementado, análogo ao de persistencia (serviço)
    @Test
    public void verificarNomesClassesCamadaServico() {

        ArchRule rule = classes()
        .that().haveSimpleNameEndingWith("Service")
        .should().resideInAPackage("..service..");

        rule.check(importedClasses);

    }
    //teste novo implementado, análogo ao de persistencia (serviço)
    @Test
    public void verificarImplementacaoInterfaceService() {

        ArchRule rule = classes()
        .that().implement(Service.class)
        .should().haveSimpleNameEndingWith("Service");

        rule.check(importedClasses);

    }
    //teste novo implementado, análogo ao de violação camada 
    @Test
    public void verificarOutraViolacaoCamadas() {

        ArchRule rule = layeredArchitecture()
        .layer("Business").definedBy("..business..")
        .layer("Service").definedBy("..service..")
            
        .whereLayer("Service").mayOnlyBeAccessedByLayers("Business");

        rule.check(importedClasses);

    }

}
