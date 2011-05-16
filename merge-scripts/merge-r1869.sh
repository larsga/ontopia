
# directories are being deleted after move, forcefully remove directories

# bring back original version of tree-conflicted directories
svn resolve --accept working trunk/ontopia-maven/ontopia-db2tm/src/site
svn resolve --accept working trunk/ontopia-maven/webapp-accessctl/src/site
svn resolve --accept working trunk/ontopia-maven/ontopia-classify/src/site
svn resolve --accept working trunk/ontopia-maven/ontopia-vizigator/src/site
svn resolve --accept working trunk/ontopia-maven/ontopia-tmrap/src/site
svn resolve --accept working trunk/ontopia-maven/webapp-i18n/src/site
svn resolve --accept working trunk/ontopia-maven/webapp-xmltools/src/site
svn resolve --accept working trunk/ontopia-maven/ontopia-webed/src/site
svn resolve --accept working trunk/ontopia-maven/webapp-root/src/site
svn resolve --accept working trunk/ontopia-maven/webapp-ontopoly/src/site
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/site
svn resolve --accept working trunk/ontopia-maven/ontopia-navigator/src/site
svn resolve --accept working trunk/ontopia-maven/webapp-manage/src/site
svn resolve --accept working trunk/ontopia-maven/ontopia-realm/src/site
svn resolve --accept working trunk/ontopia-maven/ontopia-distribution-tomcat/src/site
svn resolve --accept working trunk/ontopia-maven/cxtm-tests/src/site
svn resolve --accept working trunk/ontopia-maven/webapp-omnigator/src/site

# forcefully delete tree-conflicted directories
svn delete --force trunk/ontopia-maven/ontopia-db2tm/src/site
svn delete --force trunk/ontopia-maven/webapp-accessctl/src/site
svn delete --force trunk/ontopia-maven/ontopia-classify/src/site
svn delete --force trunk/ontopia-maven/ontopia-vizigator/src/site
svn delete --force trunk/ontopia-maven/ontopia-tmrap/src/site
svn delete --force trunk/ontopia-maven/webapp-i18n/src/site
svn delete --force trunk/ontopia-maven/webapp-xmltools/src/site
svn delete --force trunk/ontopia-maven/ontopia-webed/src/site
svn delete --force trunk/ontopia-maven/webapp-root/src/site
svn delete --force trunk/ontopia-maven/webapp-ontopoly/src/site
svn delete --force trunk/ontopia-maven/ontopia-core/src/site
svn delete --force trunk/ontopia-maven/ontopia-navigator/src/site
svn delete --force trunk/ontopia-maven/webapp-manage/src/site
svn delete --force trunk/ontopia-maven/ontopia-realm/src/site
svn delete --force trunk/ontopia-maven/ontopia-distribution-tomcat/src/site
svn delete --force trunk/ontopia-maven/cxtm-tests/src/site
svn delete --force trunk/ontopia-maven/webapp-omnigator/src/site
