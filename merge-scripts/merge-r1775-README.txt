DONE	svn move ontopia/src/webapps/ontopoly/ontopoly-editor ontopia-maven/ontopoly-editor
DONE	svn move ontopia/src/webapps/ontopoly/ontopoly-webapp-base ontopia-maven/webapp-ontopoly
DONE	svn move ontopia/src/webapps/ontopoly/ontopoly-webapp-standalone ontopia-maven/webapp-ontopoly-standalone
DONE	# remaining: pom.xml and readme.txt (readme.txt could go to webapp-standalone)
	changes in parent pom.xml:
DONE		added <module>webapp-ontopoly-standalone</module>
DONE		added <module>ontopoly-editor</module>
		[added repositories.repository.snapshot=false]
	changes in ontopoly-editor/pom.xml:
DONE		changed parent to net.ontopia.maven / ontopia-parent
DONE		removed groupId and version elements for project itself (inherited from parent)
DONE		updated ontopia-engine dependency to ontopia-core + ${project.groupId} + ${project.version} # later changed to net.ontopia ontopia-engine 5.1.3
	changes in ontopoly-editor:
DONE		changed java/ontopoly/utils/ExportUtils.java # undone due to ontopia-engine 5.1.3 dependency
DONE			import net.ontopia.topicmaps.nav.utils.deciders.TMExporterDecider;
DONE			import net.ontopia.topicmaps.utils.deciders.TMExporterDecider;
	changes in webapp-ontopoly/pom.xml
DONE		changed parent to net.ontopia.maven / ontopia-parent
DONE		removed groupId and version elements for project itself (inherited from parent)
DONE		updated artifactId to webapp-ontopoly (was ontopoly-webapp-base)
DONE		updated ontopoly-editor dependency settings to ${project.groupId} + ${project.version}
DONE		removed provided ontopia-engine dependency (transitive)
DONE		removed ontopoly-rest dependency (non-existent and unneeded)
	changes in webapp-ontopoly-standalone/pom.xml
DONE		changed parent to net.ontopia.maven / ontopia-parent
DONE		removed groupId and version elements for project itself (inherited from parent)
DONE		updated artifactId to webapp-ontopoly-standalone (was ontopoly-webapp-standalone)
DONE		updated webapp-ontopoly dependency settings to ${project.groupId} + ${project.version}
DONE		removed ontopia-engine dependency (transitive)
DONE		removed slf4j-jdk14 dependency ($$$$$$$$$$$$$$ TODO: verify this $$$$$$$$$$$$$$)
DONE		updated overlay: net.ontopia / ontopoly-webapp-base to ${project.groupId} / webapp-ontopoly
-SKIPPED	added temporary dependency: net.ontopia.maven / ontopia-core
-SKIPPED	added temporary dependency: javax.servlet / servlet-api
DONE		$$$$$$$$$$$$$$ updated h2 dependency to scope: compile $$$$$$$$$$$$$$
        

	
		

	tests run:
		mvn clean install
			// results: 
		mvn clean site
			// results: 

	Ontopoly standalone
		BUG: Winstone uses tmp dir that is unavailable (due to +++ in path -> '   ') :: using -Djava.io.tmpdir=/path/to/own/tmp/dir fixes this
		ISSUE: Ontopoly topic maps: missing all topic maps
	