
# directory is being deleted after move, forcefully remove directory

# bring back original version of tree-conflicted directory
svn resolve --accept working trunk/ontopia-maven/webapp-ontopoly/src/docbook/ontopoly/images
svn resolve --accept working trunk/ontopia-maven/webapp-omnigator/src/docbook/omnigator/graphics

# forcefully delete tree-conflicted directory
svn delete --force trunk/ontopia-maven/webapp-ontopoly/src/docbook/ontopoly/images
svn delete --force trunk/ontopia-maven/webapp-omnigator/src/docbook/omnigator/graphics
