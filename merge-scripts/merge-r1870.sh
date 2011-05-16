
# directories are being deleted after move, forcefully remove directories

# bring back original version of tree-conflicted directories
svn resolve --accept working trunk/ontopia-maven/src/site
svn resolve --accept working trunk/ontopia-maven/ontopia-contentstore/src/site
svn resolve --accept working trunk/ontopia-maven/cxtm-tests/src

# forcefully delete tree-conflicted directories
svn delete --force trunk/ontopia-maven/src/site
svn delete --force trunk/ontopia-maven/ontopia-contentstore/src/site
svn delete --force trunk/ontopia-maven/cxtm-tests/src
