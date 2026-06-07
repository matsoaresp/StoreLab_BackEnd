package com.unifil.appstore.utils;

/**
 * DOCUMENTAÇÃO DE AUTENTICAÇÃO - AppStore
 *
 * Este arquivo documenta como usar o sistema de autenticação com JWT
 *
 * ============================================================
 * 1. CRIAR USUÁRIO ALUNO (STUDENT)
 * ============================================================
 *
 * POST /usuario/aluno
 * Content-Type: application/json
 *
 * {
 *   "nome": "João Silva",
 *   "email": "joao@example.com",
 *   "matricula": "2024001",
 *   "senha": "senha123"
 * }
 *
 * Resposta (201 Created):
 * {
 *   "id": 1,
 *   "login": "joaosilva",
 *   "nome": "João Silva",
 *   "email": "joao@example.com",
 *   "matricula": "2024001",
 *   "role": "STUDENT",
 *   "ativo": true,
 *   "dataCriacao": "2024-01-15T10:30:00"
 * }
 *
 * ============================================================
 * 2. CRIAR USUÁRIO ADMIN (PROFESSOR)
 * ============================================================
 *
 * POST /usuario/professor
 * Content-Type: application/json
 *
 * {
 *   "nome": "Prof. Maria",
 *   "email": "maria@example.com",
 *   "matricula": "PROF001",
 *   "senha": "senha456"
 * }
 *
 * Resposta (201 Created):
 * {
 *   "id": 2,
 *   "login": "profmaria",
 *   "nome": "Prof. Maria",
 *   "email": "maria@example.com",
 *   "matricula": "PROF001",
 *   "role": "ADMIN",
 *   "ativo": true,
 *   "dataCriacao": "2024-01-15T10:30:00"
 * }
 *
 * ============================================================
 * 3. FAZER LOGIN (AUTENTICAÇÃO)
 * ============================================================
 *
 * POST /auth/login
 * Content-Type: application/json
 *
 * {
 *   "login": "joaosilva",
 *   "senha": "senha123"
 * }
 *
 * Resposta (200 OK):
 * {
 *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
 *   "tipo": "Bearer",
 *   "userId": 1,
 *   "nome": "João Silva",
 *   "email": "joao@example.com",
 *   "role": "STUDENT",
 *   "tempoExpiracao": 86400
 * }
 *
 * ============================================================
 * 4. USAR TOKEN EM REQUISIÇÕES PROTEGIDAS
 * ============================================================
 *
 * GET /usuario/{id}
 * Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
 *
 * Todos os endpoints /usuario/** requerem autenticação
 *
 * ============================================================
 * 5. CONTROLE DE ACESSO POR ROLE
 * ============================================================
 *
 * ENDPOINTS PÚBLICOS (sem autenticação):
 * - POST /auth/login
 * - POST /usuario/aluno (criar conta de aluno)
 * - POST /usuario/professor (criar conta de professor)
 * - GET /swagger-ui/**
 * - GET /v3/api-docs/**
 *
 * ENDPOINTS RESTRITOS PARA ADMIN:
 * - DELETE /usuario/admin/** (apenas admins podem deletar)
 *
 * ENDPOINTS PARA STUDENT OU ADMIN:
 * - GET /usuario/{id}
 * - GET /usuario/todos
 * - PUT /usuario/atualizar/{id}
 *
 * ============================================================
 * 6. VALIDAR TOKEN
 * ============================================================
 *
 * GET /auth/validar-token
 * Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
 *
 * Resposta (200 OK):
 * "Token válido"
 *
 * ============================================================
 * 7. OBTER INFORMAÇÕES DO USUÁRIO DO TOKEN
 * ============================================================
 *
 * GET /auth/info-usuario
 * Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
 *
 * Resposta (200 OK):
 * {
 *   "id": 1,
 *   "login": "joaosilva",
 *   "nome": "João Silva",
 *   "email": "joao@example.com",
 *   "matricula": "2024001",
 *   "role": "STUDENT",
 *   "ativo": true,
 *   "dataCriacao": "2024-01-15T10:30:00"
 * }
 *
 * ============================================================
 * 8. LOGOUT
 * ============================================================
 *
 * POST /auth/logout
 * Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
 *
 * Resposta (200 OK):
 * "Logout realizado com sucesso"
 *
 * NOTA: O logout é apenas informativo. O JWT permanece válido
 * até expirar. Para implementar logout real, seria necessário
 * manter uma lista de tokens revogados no servidor.
 *
 * ============================================================
 * 9. CONFIGURAÇÕES JWT
 * ============================================================
 *
 * No arquivo application.properties:
 *
 * api.security.token.secret=sua-chave-secreta-super-segura-aqui-minimo-32-caracteres
 * api.security.token.expiration=24
 *
 * - secret: Chave usada para assinar os tokens (MUDE ISSO EM PRODUÇÃO!)
 * - expiration: Tempo de expiração do token em HORAS
 *
 * ============================================================
 * 10. AUTORIDADES/ROLES
 * ============================================================
 *
 * ADMIN:
 * - ROLE_ADMIN
 * - ROLE_STUDENT
 *
 * STUDENT:
 * - ROLE_STUDENT
 *
 * VISITOR:
 * - ROLE_VISITOR
 *
 * ============================================================
 * 11. CRIPTOGRAFIA DE SENHA
 * ============================================================
 *
 * As senhas são criptografadas com BCrypt com salt aleatório.
 * Cada vez que um usuário se registra, a senha é hash com
 * uma salt diferente, garantindo segurança mesmo se dois
 * usuários tiverem a mesma senha.
 *
 * ============================================================
 * 12. EXEMPLOS DE ERRO
 * ============================================================
 *
 * ERRO 401 - Não autenticado:
 * Token ausente ou inválido
 *
 * ERRO 403 - Não autorizado:
 * Usuário autenticado mas sem permissão para acessar o recurso
 *
 * ERRO 400 - Dados inválidos:
 * Campos obrigatórios ausentes ou inválidos
 *
 * ============================================================
 */

public class AuthenticationDocumentation {

}

