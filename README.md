# JIRA Tempo Client

### Build

```
./gradlew clean check
```

### Run

```
./gradlew bootRun
```

### MCP

If possible, prefer streamable protocol to SSE.


#### SSE Transport

```yaml
spring:
  ai:
    mcp:
      server:
        sse-endpoint: /sse
        protocol: sse
```

```bash
claude mcp add --scope user --transport sse nips http://localhost:8080/sse
```

#### Streaming Transport

```yaml
spring:
  application:
    name: eden-research
  threads.virtual:
    enabled: true
  ai:
    mcp:
      server:
        name: nip-server
        version: 0.0.1
        type: sync
        capabilities:
          tool: true
          resource: false
          prompt: false
          completion: false
        protocol: streamable
        streamable-http:
          mcp-endpoint: /mcp
```

```bash
claude mcp add --scope user --transport http nips http://localhost:8080/mcp
```
