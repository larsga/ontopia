
# bring back original trunk versions of tree-conflicted files
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/utils/ctm/ctm.flex
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/utils/ctm/ctm.g
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/query/parser/tolog.g

# forcefully delete branch version of tree-conflicted files - no textual changes were applied in branch in this revision
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/jflex/net/ontopia/topicmaps/utils/ctm/ctm.flex
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/antlr/net/ontopia/topicmaps/utils/ctm/ctm.g
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/antlr/net/ontopia/topicmaps/query/parser/tolog.g

# re-apply svn:move for tree-conflicted files to include changes in trunk in revisions 1183:HEAD
svn move trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/utils/ctm/ctm.flex   trunk/ontopia-maven/ontopia-core/src/main/jflex/net/ontopia/topicmaps/utils/ctm/ctm.flex
svn move trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/utils/ctm/ctm.g      trunk/ontopia-maven/ontopia-core/src/main/antlr/net/ontopia/topicmaps/utils/ctm/ctm.g
svn move trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/query/parser/tolog.g trunk/ontopia-maven/ontopia-core/src/main/antlr/net/ontopia/topicmaps/query/parser/tolog.g




svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/utils/jtm
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/utils/ltm

svn delete --force trunk/ontopia-maven/ontopia-core/src/main/jflex/net/ontopia/topicmaps/utils/jtm
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/antlr/net/ontopia/topicmaps/utils/ltm

svn move trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/utils/jtm trunk/ontopia-maven/ontopia-core/src/main/jflex/net/ontopia/topicmaps/utils/jtm
svn move trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/utils/ltm trunk/ontopia-maven/ontopia-core/src/main/antlr/net/ontopia/topicmaps/utils/ltm
