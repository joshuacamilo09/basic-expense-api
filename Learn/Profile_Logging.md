O Spring boot usa profiles para ativar configurações diferentes por ambiente
       e a configuração externa pode vir de ficheiros, variáveis de ambiente
       e argumentos de linha de comandos. Além disso, quando usamos os starters 
        do spring boot, o LogBack é o motor de logging por omissão.

# Profile
Serve para dizer: 
- Em dev usa tal config
- Em test usa outra
- E em prod usa outra também

Exemplo: 
- em dev posso mostrar sql no terminal 
- em prod posso não mostrar sql
- em test posso usar outra base de dados



# Logging 

Logging permite: 
- filtrar por níveis 
- separar logs por package
- desligar ruído em produção
- integrar com ferramentas de observabilities

Níveis de log: 
- ERROR: erro sério
- WARN: algo suspeito/importante
- INFO: eventos normais importantes
- DEBUG: detalhe técnico para desenvolvimento
- TRACE: detalhe extremo

O Spring Boot documenta que podemos controlar estes níveis por logger/package através das propriedades de logging.

** Regra prática **

- INFO para eventos importantes do fluxo
- DEBUG para detalhe técnico
- WARN para ações sensíveis ou estados anómalos
- ERROR para falhas reais