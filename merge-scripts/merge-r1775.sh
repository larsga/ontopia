
# reapply ontopoly moves

# revert original move
svn revert --depth infinity trunk/ontopia/src/webapps/ontopoly
svn revert --depth infinity trunk/ontopia-maven/webapp-ontopoly
rm -rf trunk/ontopia-maven/webapp-ontopoly

# reapply ontopoly moves to three separate maven modules
svn move trunk/ontopia/src/webapps/ontopoly/ontopoly-editor            trunk/ontopia-maven/ontopoly-editor
svn move trunk/ontopia/src/webapps/ontopoly/ontopoly-webapp-base       trunk/ontopia-maven/webapp-ontopoly
svn move trunk/ontopia/src/webapps/ontopoly/ontopoly-webapp-standalone trunk/ontopia-maven/webapp-ontopoly-standalone

# move README file to standalone ontopoly webapp
svn move trunk/ontopia/src/webapps/ontopoly/README.txt                  trunk/ontopia-maven/webapp-ontopoly-standalone/

# remove top level ontopoly directory
svn delete trunk/ontopia/src/webapps/ontopoly

# reapply changes to individual files
svn merge -c 1775 branches/ontopia-maven/ontopia-maven/webapp-ontopoly/src/main/java/ontopoly/utils/ExportUtils.java trunk/ontopia-maven/ontopoly-editor/src/main/java/ontopoly/utils/ExportUtils.java

# apply patches to 4 pom files
patch -p0 -i merge-r1775-patch.diff 
