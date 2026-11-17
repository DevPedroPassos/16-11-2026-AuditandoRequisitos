import java.util.Scanner;

public class SistemaSupervisorio {

    // ===== Entrada de dados =====
    static Scanner sc = new Scanner(System.in);

    // ===== Variáveis do sistema (guardam o "estado" da máquina) =====
    static String instituicao = "SENAI";
    static String usuario = "";
    static boolean logado = true;

    static boolean emergencia = false;            // RF-01
    static String statusRobo = "PARADO";          // RF-02
    static String statusPrograma = "PARADO";      // RF-03
    static boolean comunicacaoOk = true;          // RF-04

    static int pecasProcessadas = 0;              // RF-05
    static int pecasBoas = 0;
    static int pecasRuins = 0;

    static int totalCiclos = 0;                   // RF-06
    static double somaTempoCiclo = 0;

    static boolean cameraLigada = false;          // RF-08
    static boolean gravando = false;              // RF-09
    static String statusCamera = "AGUARDANDO";    // RF-10

    static double tempoPlanejado = 0;             // RF-12 e RF-13 (em minutos)
    static double tempoParado = 0;

    static String[] notificacoes = new String[50]; // RF-18
    static int qtdNotificacoes = 0;

    // =====================================================
    // PROGRAMA PRINCIPAL
    // =====================================================
    public static void main(String[] args) {

        rf16LogotipoInstituicao();

        System.out.print("Digite seu nome de usuário: ");
        usuario = sc.nextLine();

        while (logado) {
            rf15Sidebar();
            System.out.print("Escolha uma opção: ");
            int opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    rf01BotaoEmergencia();
                    break;
                case 2:
                    rf02StatusRobo();
                    break;
                case 3:
                    rf03StatusPrograma();
                    break;
                case 4:
                    rf04StatusComunicacao();
                    break;
                case 5:
                    rf05ContadorPecas();
                    break;
                case 6:
                    rf06TempoCiclo();
                    break;
                case 7:
                    rf07TaxaAcerto();
                    break;
                case 8:
                    rf08VisualizacaoCamera();
                    break;
                case 9:
                    rf09IndicadorGravacao();
                    break;
                case 10:
                    rf10StatusProcessamentoCamera();
                    break;
                case 12:
                    rf12EficienciaOEE();
                    break;
                case 13:
                    rf13DisponibilidadeMecanica();
                    break;
                case 14:
                    rf14PainelMonitoramento();
                    break;
                case 17:
                    rf17RelogioSistema();
                    break;
                case 18:
                    rf18CentralNotificacoes();
                    break;
                case 19:
                    rf19PerfilLogout();
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        }

        System.out.println("Sistema encerrado. Até logo!");
        sc.close();
    }

    // =====================================================
    // MÉTODOS AUXILIARES
    // =====================================================

    // Lê um número inteiro e já limpa a linha do Enter
    public static int lerInteiro() {
        int numero = sc.nextInt();
        sc.nextLine();
        return numero;
    }

    // Lê um número decimal e já limpa a linha do Enter
    public static double lerDecimal() {
        double numero = sc.nextDouble();
        sc.nextLine();
        return numero;
    }

    // Guarda uma mensagem na lista de notificações (usado por vários RFs)
    public static void adicionarNotificacao(String mensagem) {
        if (qtdNotificacoes < 50) {
            notificacoes[qtdNotificacoes] = mensagem;
            qtdNotificacoes++;
        }
    }

    // Calcula a taxa de acerto em porcentagem
    public static double calcularTaxaAcerto() {
        if (pecasProcessadas == 0) {
            return 0;
        }
        return pecasBoas * 100.0 / pecasProcessadas;
    }

    // Calcula o tempo médio de ciclo em segundos
    public static double calcularTempoMedioCiclo() {
        if (totalCiclos == 0) {
            return 0;
        }
        return somaTempoCiclo / totalCiclos;
    }

    // =====================================================
    // RF-01 - Botão de Parada de Emergência
    // =====================================================
    public static void rf01BotaoEmergencia() {
        System.out.println("\n--- BOTÃO DE PARADA DE EMERGÊNCIA ---");

        if (emergencia) {
            System.out.println("A EMERGÊNCIA ESTÁ ATIVA!");
            System.out.println("1 - Liberar emergência");
            System.out.println("2 - Manter parado");
            int opcao = lerInteiro();

            if (opcao == 1) {
                emergencia = false;
                adicionarNotificacao("Emergência liberada por " + usuario);
                System.out.println("Emergência liberada. O robô continua PARADO até você iniciá-lo.");
            }
        } else {
            System.out.println("1 - APERTAR BOTÃO DE EMERGÊNCIA");
            System.out.println("2 - Voltar");
            int opcao = lerInteiro();

            if (opcao == 1) {
                emergencia = true;
                statusRobo = "PARADO (EMERGENCIA)";
                statusPrograma = "PARADO";
                gravando = false;
                adicionarNotificacao("EMERGÊNCIA acionada por " + usuario);
                System.out.println("!!! PARADA DE EMERGÊNCIA ACIONADA !!!");
            }
        }
    }

    // =====================================================
    // RF-02 - Exibição do Status do Robô
    // =====================================================
    public static void rf02StatusRobo() {
        System.out.println("\n--- STATUS DO ROBÔ ---");
        System.out.println("Status atual: " + statusRobo);
        System.out.println("Alterar status:");
        System.out.println("1 - Operando");
        System.out.println("2 - Parado");
        System.out.println("3 - Com erro");
        System.out.println("4 - Voltar");
        int opcao = lerInteiro();

        switch (opcao) {
            case 1:
                if (emergencia) {
                    System.out.println("Não é possível operar: emergência ativa!");
                } else {
                    statusRobo = "OPERANDO";
                }
                break;
            case 2:
                statusRobo = "PARADO";
                break;
            case 3:
                statusRobo = "COM ERRO";
                adicionarNotificacao("Robô entrou em estado de ERRO");
                break;
            default:
                break;
        }
        System.out.println("Status do robô: " + statusRobo);
    }

    // =====================================================
    // RF-03 - Status do Programa
    // =====================================================
    public static void rf03StatusPrograma() {
        System.out.println("\n--- STATUS DO PROGRAMA ---");
        System.out.println("Status atual: " + statusPrograma);
        System.out.println("1 - Executando");
        System.out.println("2 - Pausado");
        System.out.println("3 - Parado");
        System.out.println("4 - Voltar");
        int opcao = lerInteiro();

        if (opcao == 1) {
            if (emergencia) {
                System.out.println("Não é possível executar: emergência ativa!");
            } else {
                statusPrograma = "EXECUTANDO";
            }
        } else if (opcao == 2) {
            statusPrograma = "PAUSADO";
        } else if (opcao == 3) {
            statusPrograma = "PARADO";
        }
        System.out.println("Status do programa: " + statusPrograma);
    }

    // =====================================================
    // RF-04 - Status da Comunicação
    // =====================================================
    public static void rf04StatusComunicacao() {
        System.out.println("\n--- STATUS DA COMUNICAÇÃO ---");
        if (comunicacaoOk) {
            System.out.println("Comunicação: CONECTADO");
        } else {
            System.out.println("Comunicação: DESCONECTADO");
        }

        System.out.println("1 - Simular conexão");
        System.out.println("2 - Simular desconexão");
        System.out.println("3 - Voltar");
        int opcao = lerInteiro();

        if (opcao == 1) {
            comunicacaoOk = true;
            System.out.println("Comunicação restabelecida.");
        } else if (opcao == 2) {
            comunicacaoOk = false;
            adicionarNotificacao("Falha de comunicação com o robô");
            System.out.println("Comunicação perdida!");
        }
    }

    // =====================================================
    // RF-05 - Contador de Peças Processadas
    // =====================================================
    public static void rf05ContadorPecas() {
        System.out.println("\n--- CONTADOR DE PEÇAS ---");
        System.out.println("Quantas peças deseja registrar?");
        int quantidade = lerInteiro();

        for (int i = 1; i <= quantidade; i++) {
            System.out.println("Peça " + i + ": 1 - Boa | 2 - Ruim");
            int resultado = lerInteiro();

            if (resultado == 1) {
                pecasBoas++;
            } else {
                pecasRuins++;
            }
            pecasProcessadas++;
        }

        System.out.println("Total processado: " + pecasProcessadas);
        System.out.println("Boas: " + pecasBoas + " | Ruins: " + pecasRuins);
    }

    // =====================================================
    // RF-06 - Medição do Tempo de Ciclo
    // =====================================================
    public static void rf06TempoCiclo() {
        System.out.println("\n--- TEMPO DE CICLO ---");
        System.out.println("Quantos ciclos deseja registrar?");
        int quantidade = lerInteiro();

        for (int i = 1; i <= quantidade; i++) {
            System.out.print("Tempo do ciclo " + i + " (segundos): ");
            double tempo = lerDecimal();
            somaTempoCiclo = somaTempoCiclo + tempo;
            totalCiclos++;
        }

        System.out.println("Total de ciclos: " + totalCiclos);
        System.out.println("Tempo médio de ciclo: " + calcularTempoMedioCiclo() + " s");
    }

    // =====================================================
    // RF-07 - Indicador de Taxa de Acerto
    // =====================================================
    public static void rf07TaxaAcerto() {
        System.out.println("\n--- TAXA DE ACERTO ---");
        if (pecasProcessadas == 0) {
            System.out.println("Nenhuma peça processada ainda.");
        } else {
            System.out.println("Peças boas: " + pecasBoas + " de " + pecasProcessadas);
            System.out.println("Taxa de acerto: " + calcularTaxaAcerto() + " %");
        }
    }

    // =====================================================
    // RF-08 - Visualização de Câmera/Vídeo
    // =====================================================
    public static void rf08VisualizacaoCamera() {
        System.out.println("\n--- CÂMERA / VÍDEO ---");
        if (cameraLigada) {
            System.out.println("Câmera: LIGADA");
            System.out.println("+----------------------------+");
            System.out.println("|   [IMAGEM DA CÂMERA]       |");
            System.out.println("|   (simulação de vídeo)     |");
            System.out.println("+----------------------------+");
            System.out.println("1 - Desligar câmera");
        } else {
            System.out.println("Câmera: DESLIGADA");
            System.out.println("1 - Ligar câmera");
        }
        System.out.println("2 - Voltar");
        int opcao = lerInteiro();

        if (opcao == 1) {
            if (cameraLigada) {
                cameraLigada = false;
                gravando = false; // sem câmera não há gravação
            } else {
                cameraLigada = true;
            }
        }
    }

    // =====================================================
    // RF-09 - Indicador de Gravação de Vídeo
    // =====================================================
    public static void rf09IndicadorGravacao() {
        System.out.println("\n--- GRAVAÇÃO DE VÍDEO ---");
        if (gravando) {
            System.out.println("(REC) GRAVANDO...");
            System.out.println("1 - Parar gravação");
        } else {
            System.out.println("Gravação: PARADA");
            System.out.println("1 - Iniciar gravação");
        }
        System.out.println("2 - Voltar");
        int opcao = lerInteiro();

        if (opcao == 1) {
            if (gravando) {
                gravando = false;
                System.out.println("Gravação parada.");
            } else if (cameraLigada) {
                gravando = true;
                System.out.println("(REC) Gravação iniciada!");
            } else {
                System.out.println("Ligue a câmera primeiro (opção 8).");
            }
        }
    }

    // =====================================================
    // RF-10 - Status de Processamento da Câmera
    // =====================================================
    public static void rf10StatusProcessamentoCamera() {
        System.out.println("\n--- PROCESSAMENTO DA CÂMERA ---");
        System.out.println("Status atual: " + statusCamera);
        System.out.println("1 - Aguardando");
        System.out.println("2 - Processando imagem");
        System.out.println("3 - Concluído");
        System.out.println("4 - Erro");
        System.out.println("5 - Voltar");
        int opcao = lerInteiro();

        switch (opcao) {
            case 1:
                statusCamera = "AGUARDANDO";
                break;
            case 2:
                statusCamera = "PROCESSANDO";
                break;
            case 3:
                statusCamera = "CONCLUÍDO";
                break;
            case 4:
                statusCamera = "ERRO";
                adicionarNotificacao("Erro no processamento da câmera");
                break;
            default:
                break;
        }
        System.out.println("Status da câmera: " + statusCamera);
    }

    // =====================================================
    // RF-12 - Monitoramento da Eficiência (OEE)
    // OEE = Disponibilidade x Desempenho x Qualidade
    // =====================================================
    public static void rf12EficienciaOEE() {
        System.out.println("\n--- EFICIÊNCIA (OEE) ---");

        if (tempoPlanejado == 0 || pecasProcessadas == 0 || totalCiclos == 0) {
            System.out.println("Para calcular o OEE, primeiro registre:");
            System.out.println("- os tempos de produção (opção 13)");
            System.out.println("- as peças (opção 5)");
            System.out.println("- os tempos de ciclo (opção 6)");
        } else {
            System.out.print("Informe o tempo de ciclo IDEAL (segundos): ");
            double cicloIdeal = lerDecimal();

            double tempoOperando = tempoPlanejado - tempoParado;

            double disponibilidade = tempoOperando / tempoPlanejado;
            double desempenho = (cicloIdeal * pecasProcessadas) / (tempoOperando * 60);
            double qualidade = (double) pecasBoas / pecasProcessadas;

            double oee = disponibilidade * desempenho * qualidade * 100;

            System.out.println("Disponibilidade: " + disponibilidade * 100 + " %");
            System.out.println("Desempenho: " + desempenho * 100 + " %");
            System.out.println("Qualidade: " + qualidade * 100 + " %");
            System.out.println("OEE: " + oee + " %");
        }
    }

    // =====================================================
    // RF-13 - Métricas de Disponibilidade Mecânica
    // =====================================================
    public static void rf13DisponibilidadeMecanica() {
        System.out.println("\n--- DISPONIBILIDADE MECÂNICA ---");
        System.out.print("Tempo planejado de produção (minutos): ");
        tempoPlanejado = lerDecimal();
        System.out.print("Tempo em que a máquina ficou parada (minutos): ");
        tempoParado = lerDecimal();

        if (tempoPlanejado <= 0 || tempoParado > tempoPlanejado) {
            System.out.println("Valores inválidos!");
            tempoPlanejado = 0;
            tempoParado = 0;
        } else {
            double tempoOperando = tempoPlanejado - tempoParado;
            double disponibilidade = tempoOperando * 100 / tempoPlanejado;

            System.out.println("Tempo operando: " + tempoOperando + " min");
            System.out.println("Tempo parado: " + tempoParado + " min");
            System.out.println("Disponibilidade: " + disponibilidade + " %");
        }
    }

    // =====================================================
    // RF-14 - Painel de Monitoramento de Peças
    // =====================================================
    public static void rf14PainelMonitoramento() {
        System.out.println("\n========== PAINEL DE MONITORAMENTO ==========");
        System.out.println("Peças processadas : " + pecasProcessadas);
        System.out.println("Peças boas        : " + pecasBoas);
        System.out.println("Peças ruins       : " + pecasRuins);
        System.out.println("Taxa de acerto    : " + calcularTaxaAcerto() + " %");
        System.out.println("Tempo médio ciclo : " + calcularTempoMedioCiclo() + " s");
        System.out.println("Status do robô    : " + statusRobo);
        System.out.println("Status do programa: " + statusPrograma);
        if (comunicacaoOk) {
            System.out.println("Comunicação       : CONECTADO");
        } else {
            System.out.println("Comunicação       : DESCONECTADO");
        }
        System.out.println("=============================================");
    }

    // =====================================================
    // RF-15 - Navegação por Sidebar (menu lateral)
    // =====================================================
    public static void rf15Sidebar() {
        System.out.println("\n+------------- MENU -------------+");
        System.out.println(" 1  - Botão de Emergência");
        System.out.println(" 2  - Status do Robô");
        System.out.println(" 3  - Status do Programa");
        System.out.println(" 4  - Status da Comunicação");
        System.out.println(" 5  - Contador de Peças");
        System.out.println(" 6  - Tempo de Ciclo");
        System.out.println(" 7  - Taxa de Acerto");
        System.out.println(" 8  - Câmera / Vídeo");
        System.out.println(" 9  - Gravação de Vídeo");
        System.out.println(" 10 - Processamento da Câmera");
        System.out.println(" 12 - Eficiência (OEE)");
        System.out.println(" 13 - Disponibilidade Mecânica");
        System.out.println(" 14 - Painel de Monitoramento");
        System.out.println(" 17 - Relógio do Sistema");
        System.out.println(" 18 - Central de Notificações");
        System.out.println(" 19 - Perfil / Logout");
        System.out.println("+--------------------------------+");
    }

    // =====================================================
    // RF-16 - Identificação e Logotipo da Instituição
    // =====================================================
    public static void rf16LogotipoInstituicao() {
        System.out.println("**********************************");
        System.out.println("*                                *");
        System.out.println("*          " + instituicao + "                 *");
        System.out.println("*  SISTEMA SUPERVISÓRIO INDUSTRIAL  *");
        System.out.println("*                                *");
        System.out.println("**********************************");
    }

    // =====================================================
    // RF-17 - Relógio do Sistema
    // =====================================================
    public static void rf17RelogioSistema() {
        System.out.println("\n--- RELÓGIO DO SISTEMA ---");
        System.out.print("Hora (0-23): ");
        int hora = lerInteiro();
        System.out.print("Minuto (0-59): ");
        int minuto = lerInteiro();
        System.out.print("Segundo (0-59): ");
        int segundo = lerInteiro();

        if (hora < 0 || hora > 23 || minuto < 0 || minuto > 59 || segundo < 0 || segundo > 59) {
            System.out.println("Horário inválido!");
        } else {
            String h = "" + hora;
            String m = "" + minuto;
            String s = "" + segundo;

            if (hora < 10) {
                h = "0" + hora;
            }
            if (minuto < 10) {
                m = "0" + minuto;
            }
            if (segundo < 10) {
                s = "0" + segundo;
            }
            System.out.println("Horário do sistema: " + h + ":" + m + ":" + s);
        }
    }

    // =====================================================
    // RF-18 - Central de Notificações
    // =====================================================
    public static void rf18CentralNotificacoes() {
        System.out.println("\n--- CENTRAL DE NOTIFICAÇÕES ---");

        if (qtdNotificacoes == 0) {
            System.out.println("Nenhuma notificação.");
        } else {
            for (int i = 0; i < qtdNotificacoes; i++) {
                System.out.println((i + 1) + " - " + notificacoes[i]);
            }
            System.out.println("\n1 - Limpar notificações");
            System.out.println("2 - Voltar");
            int opcao = lerInteiro();

            if (opcao == 1) {
                qtdNotificacoes = 0;
                System.out.println("Notificações apagadas.");
            }
        }
    }

    // =====================================================
    // RF-19 - Perfil do Usuário e Logout
    // =====================================================
    public static void rf19PerfilLogout() {
        System.out.println("\n--- PERFIL DO USUÁRIO ---");
        System.out.println("Usuário logado: " + usuario);
        System.out.println("1 - Fazer logout (sair)");
        System.out.println("2 - Voltar");
        int opcao = lerInteiro();

        if (opcao == 1) {
            logado = false;
        }
    }
}