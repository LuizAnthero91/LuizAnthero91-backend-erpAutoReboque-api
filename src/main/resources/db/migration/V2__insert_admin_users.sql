INSERT INTO usuarios (
    nome,
    email,
    senha_hash,
    perfil,
    ativo
)VALUES
     (
         'Administrador Principal',
         'admin@autoreboquetora.com.br',
         '$2a$10$BxEcTIqF6XE7mZ/cQQsQfutCpgryP.23LeVr4XlYBNRe0hbo1/Vmy',
         'ADMIN',
         true
     ),
     (
         'Administrador Financeiro',
         'financeiro@autoreboquetora.com.br',
         '$2a$10$BxEcTIqF6XE7mZ/cQQsQfutCpgryP.23LeVr4XlYBNRe0hbo1/Vmy',
         'ADMIN',
         true
     );
