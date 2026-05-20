package edu.fipp.ativooperante;

import edu.fipp.ativooperante.entities.Usuario;
import edu.fipp.ativooperante.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class AtivooperanteApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtivooperanteApplication.class, args);
    }

    @Bean
    public CommandLineRunner initAdmin(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Busca se o admin já existe
            List<Usuario> admins = usuarioRepository.findUsuarioByEmail("admin@pm.br");

            if (admins.isEmpty()) {
                // Se não existir, cria do zero com a senha já criptografada
                Usuario admin = new Usuario();
                admin.setEmail("admin@pm.br");
                admin.setSenha(passwordEncoder.encode("123321"));
                admin.setNivel(1); // Nível 1 = Admin
                admin.setCpf("000.000.000-00"); // CPF genérico para não dar erro de unique
                usuarioRepository.save(admin);
                System.out.println("✅ Administrador padrão criado com sucesso!");
            } else {
                // Se existir, pega o primeiro da lista
                Usuario admin = admins.getFirst();

                // Verifica se a senha não começa com "$2a$" (que é o padrão do BCrypt gerado pelo Spring)
                if (!admin.getSenha().startsWith("$2a$")) {
                    // Criptografa a senha e salva novamente
                    admin.setSenha(passwordEncoder.encode("123321"));
                    usuarioRepository.save(admin);
                    System.out.println("✅ Senha do administrador corrigida e criptografada com sucesso!");
                }
            }
        };
    }
}