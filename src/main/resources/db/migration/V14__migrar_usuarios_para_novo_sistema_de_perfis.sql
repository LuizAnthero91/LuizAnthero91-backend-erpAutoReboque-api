-- =========================================================
-- 1. CADASTRAR PERMISSOES INICIAIS
-- =========================================================

INSERT INTO permissoes (codigo, descricao)
VALUES
    ('OS_VISUALIZAR', 'Visualizar ordens de serviço'),
    ('OS_CRIAR', 'Criar ordens de serviço'),
    ('OS_EDITAR', 'Editar ordens de serviço'),
    ('OS_CANCELAR', 'Cancelar ordens de serviço'),

    ('CLIENTE_VISUALIZAR', 'Visualizar clientes'),
    ('CLIENTE_CRIAR', 'Cadastrar clientes'),
    ('CLIENTE_EDITAR', 'Editar clientes'),

    ('VEICULO_VISUALIZAR', 'Visualizar veículos'),
    ('VEICULO_CRIAR', 'Cadastrar veículos'),
    ('VEICULO_EDITAR', 'Editar veículos'),

    ('FINANCEIRO_VISUALIZAR', 'Visualizar informações financeiras'),
    ('FINANCEIRO_EDITAR', 'Alterar informações financeiras'),

    ('USUARIO_VISUALIZAR', 'Visualizar usuários'),
    ('USUARIO_CRIAR', 'Cadastrar usuários'),
    ('USUARIO_EDITAR', 'Editar usuários'),
    ('USUARIO_DESATIVAR', 'Desativar usuários'),

    ('PERFIL_VISUALIZAR', 'Visualizar perfis'),
    ('PERFIL_GERENCIAR', 'Criar e alterar perfis');


-- =========================================================
-- 2. CRIAR PERFIL ADMIN
-- =========================================================

INSERT INTO perfis (
    nome,
    descricao,
    ativo
)
VALUES (
           'ADMIN',
           'Administrador do sistema',
           TRUE
       );


-- =========================================================
-- 3. DAR TODAS AS PERMISSOES AO ADMIN
-- =========================================================

INSERT INTO perfil_permissoes (
    perfil_id,
    permissao_id
)
SELECT
    p.id,
    pe.id
FROM perfis p
         CROSS JOIN permissoes pe
WHERE p.nome = 'ADMIN';


-- =========================================================
-- 4. MIGRAR USUARIOS ANTIGOS
-- =========================================================

UPDATE usuarios
SET perfil_id = (
    SELECT id
    FROM perfis
    WHERE nome = 'ADMIN'
)
WHERE perfil = 'ADMIN';


-- =========================================================
-- 5. PERFIL_ID PASSA A SER OBRIGATORIO
-- =========================================================

ALTER TABLE usuarios
    ALTER COLUMN perfil_id
        SET NOT NULL;


-- =========================================================
-- 6. REMOVER COLUNA ANTIGA
-- =========================================================

ALTER TABLE usuarios
DROP COLUMN perfil;