
# acmday-es

## 说明
- 该工程用于Elasticsearch实践，功能入口都在test目录下
- ES版本：elasticsearch-7.0.0
- 分词器版本：elasticsearch-analysis-ik-7.0.0
- kibana版本：kibana-7.0.0-linux-x86_64

## 参考文档
- [Java High Level REST Client 使用文档](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.0/java-rest-high-getting-started.html)
- [elasticsearch-head](https://github.com/mobz/elasticsearch-head.git)
- [ES下载链接](https://www.elastic.co/cn/downloads/elasticsearch)

## DDL
```sql
CREATE TABLE `student` (
  `id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(45) NOT NULL DEFAULT '""' COMMENT '姓名',
  `address` varchar(45) NOT NULL DEFAULT '""' COMMENT '家庭住址',
  `age` tinyint(4) NOT NULL DEFAULT '1' COMMENT '年龄',
  PRIMARY KEY (`id`),
  KEY `name_idx` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```

## elasticsearch.yml
```yaml
# ======================== Elasticsearch Configuration =========================
#
# NOTE: Elasticsearch comes with reasonable defaults for most settings.
#       Before you set out to tweak and tune the configuration, make sure you
#       understand what are you trying to accomplish and the consequences.
#
# The primary way of configuring a node is via this file. This template lists
# the most important settings you may want to configure for a production cluster.
#
# Please consult the documentation for further information on configuration options:
# https://www.elastic.co/guide/en/elasticsearch/reference/index.html
#
# ---------------------------------- Cluster -----------------------------------
#
# Use a descriptive name for your cluster:
#
#cluster.name: my-application
cluster.name: acmday-es
#
# ------------------------------------ Node ------------------------------------
#
# Use a descriptive name for the node:
#
node.name: node-1
#
# Add custom attributes to the node:
#
#node.attr.rack: r1
#
# ----------------------------------- Paths ------------------------------------
#
# Path to directory where to store the data (separate multiple locations by comma):
#
#path.data: /path/to/data
path.data: /home/hy/software/elasticsearch/data
#
# Path to log files:
#
#path.logs: /path/to/logs
path.logs: /home/hy/logs/es
#
# ----------------------------------- Memory -----------------------------------
#
# Lock the memory on startup:
#
#bootstrap.memory_lock: true
#
# Make sure that the heap size is set to about half the memory available
# on the system and that the owner of the process is allowed to use this
# limit.
#
# Elasticsearch performs poorly when the system is swapping the memory.
#
# ---------------------------------- Network -----------------------------------
#
# Set the bind address to a specific IP (IPv4 or IPv6):
#
#network.host: 192.168.0.1
network.host: 127.0.0.1
#
# Set a custom port for HTTP:
#
#http.port: 9200
http.port: 9200
#
# For more information, consult the network module documentation.
#
# --------------------------------- Discovery ----------------------------------
#
# Pass an initial list of hosts to perform discovery when this node is started:
# The default list of hosts is ["127.0.0.1", "[::1]"]
#
#discovery.seed_hosts: ["host1", "host2"]
#
# Bootstrap the cluster using an initial set of master-eligible nodes:
#
#cluster.initial_master_nodes: ["node-1", "node-2"]
#
# For more information, consult the discovery and cluster formation module documentation.

# 跨域
http.cors.enabled: true
http.cors.allow-origin: "*"

# 这个属性表示节点是否具有成为主节点的资格，注意：此属性的值为true，并不意味着这个节点就是主节点。
# 因为真正的主节点，是由多个具有主节点资格的节点进行选举产生的。所以，这个属性只是代表这个节点是不是具有主节点选举资格。
node.master: true

# 这个属性表示节点是否存储数据。
node.data: true	
#
# ---------------------------------- Gateway -----------------------------------
#
# Block initial recovery after a full cluster restart until N nodes are started:
#
#gateway.recover_after_nodes: 3
#
# For more information, consult the gateway module documentation.
#
# ---------------------------------- Various -----------------------------------
#
# Require explicit names when deleting indices:
#
#action.destructive_requires_name: true
```