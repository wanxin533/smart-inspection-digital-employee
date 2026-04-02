# 知识图谱

## 用途

构建电力设备知识图谱，支持缺陷根因分析、维修建议推荐。

## 图谱结构

### 实体类型

- 设备 (Device)
- 部件 (Component)
- 缺陷 (Defect)
- 原因 (Cause)
- 措施 (Measure)

### 关系类型

- 包含 (contains)
- 导致 (causes)
- 解决方案 (solved_by)
- 相关 (related_to)

## 技术栈

- **图数据库**: Neo4j 5.x
- **查询语言**: Cypher
- **Python 库**: neo4j, py2neo

## 快速启动

```bash
docker run -d --name neo4j \
  -p 7474:7474 -p 7687:7687 \
  -e NEO4J_AUTH=neo4j/admin123 \
  neo4j:5
```

## 示例查询

```cypher
// 查询某类缺陷的所有原因
MATCH (d:Defect {type: "绝缘子破损"})-[:CAUSED_BY]->(c:Cause)
RETURN c.name, c.probability
ORDER BY c.probability DESC
```
