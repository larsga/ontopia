
# directory is being deleted after move, forcefully remove directory

# bring back original version of tree-conflicted directory
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/docbook/ltm
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/docbook/rdf2tm

# forcefully delete tree-conflicted directory
svn delete --force trunk/ontopia-maven/ontopia-core/src/docbook/ltm
svn delete --force trunk/ontopia-maven/ontopia-core/src/docbook/rdf2tm
