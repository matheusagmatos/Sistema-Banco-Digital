package src;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public abstract class Conta {

  static Scanner input = new Scanner(System.in);
  static Random aleatory = new Random();
  static Locale localeBR = new Locale("pt", "BR");

  static NumberFormat realBrasileiro = NumberFormat.getCurrencyInstance(localeBR); // essa variável irá armazenar a
                                                                                   // formatação dos valores em na moeda
                                                                                   // 'Real Brasileiro'

  private String titular;
  private String cpf;
  private String agencia;
  private int numConta;
  private int senhaConta;
  private double saldo;

  // Construtor
  public Conta(String titular, String cpf, String agencia, int numConta, int senhaConta, double saldo) {
    this.setTitular(titular);
    this.setCpf(cpf);
    this.setAgencia(agencia);
    this.setNumConta(numConta);
    this.setSenhaConta(senhaConta);
  }

  // Métodos Getter & Setter
  public int getSenhaConta() {
    return senhaConta;
  }

  public void setSenhaConta(int senhaConta) {
    this.senhaConta = senhaConta;
  }

  public int getNumConta() {
    return numConta;
  }

  public void setNumConta(int numConta) {
    this.numConta = numConta;
  }

  public String getAgencia() {
    return agencia;
  }

  public void setAgencia(String agencia) {
    this.agencia = agencia;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getTitular() {
    return titular;
  }

  public void setTitular(String titular) {
    this.titular = titular;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

  /**
   * Método utilizado para realizar transferências entre contas.
   * <p>
   * O usuário informa a conta que irá receber a transferência.
   * O método busca no ArrayList 'listaContas' se esta conta realmente
   * existe e prossegue com a transferência.
   * 
   * @param contaOrigem a conta que está realizando a tranferência
   * @param listaContas a lista de todas as contas cadastradas no banco
   * @param args  utilizamos este parâmeto para chamar o método main do programa,
   *              pois
   *              neste método está o 'menu principal'
   */
  protected static void transferir(Conta contaOrigem, ArrayList<Conta> listaContas, String[] args) {

    System.out.println("Seu saldo disponível: " + realBrasileiro.format(contaOrigem.saldo));
    System.out.println("Digite o valor da transferência: ");
    double valorDigitado = input.nextDouble();
    Conta contaDestino = null;
    System.out.println("Digite o número da conta de destino: ");
    int contaDigitada = input.nextInt();

    // Aqui percorre a lista de conta até encontrar o número da conta destino
    boolean contaExiste = false;
    for (int i = 0; i < listaContas.size(); i++) {
      if (listaContas.get(i).numConta == contaDigitada) {
        contaDestino = listaContas.get(i);
        contaExiste = true;
        break;
      }
    }

    // Caso o número da conta não seja encontrado
    if (contaExiste == false) {
      System.out.println("Número de conta destino inválido!");
      Menu.main(args);
    }

    System.out.println("Transferência de " + realBrasileiro.format(valorDigitado) + "\npara " + contaDestino.titular
        + " Conta " + contaDestino.numConta);
    System.out.println();
    System.out.println("CONFIRMA ? 1-S/2-N");
    int option = input.nextInt();

    if (option == 1) {
      contaDestino.saldo = contaDestino.saldo + valorDigitado;
      contaOrigem.saldo = contaOrigem.saldo - valorDigitado;
      System.out.println();
      System.out.println("TRANSAÇÃO EFETUADA COM SUCESSO");
      System.out.println();
      // Imprime dados da operação realizada
      System.out.println("----- TRANSFERÊNCIA -----");
      System.out.println("Origem: " + contaOrigem.titular);
      System.out.println("Conta Destino: " + contaDestino.titular + " / Conta: " + contaDestino.numConta);
      System.out.println("Valor Transferido: " + realBrasileiro.format(valorDigitado));
      System.out.println();
      try {
        ExtratoBancario.extratoTransferencia(contaOrigem, contaDestino, valorDigitado);
      } catch (IOException e) {
        e.printStackTrace();
      }
      Menu.main(args);
    } else {
      System.out.println("TRANSFERÊNCIA CANCELADA!");
      Menu.main(args);
    }
  }

  /**
   * Método utilizado para realizar saques em conta (Poupança ou Corrente)
   * <p>
   * O valor do saque é informado pelo usuário e debitado da conta do mesmo
   * 
   * @param conta a conta do usuário (Corrente ou Poupança)
   * 
   * @param args  utilizamos este parâmeto para chamar o método main do programa,
   *              pois
   *              neste método está o 'menu principal'
   */
  protected static void sacar(Conta conta, String[] args) {

    System.out.println("--------------------------------");
    System.out.println("SEU SALDO ATUAL: " + realBrasileiro.format(conta.getSaldo()));
    System.out.println("--------------------------------");
    System.out.println("");
    System.out.println("QUAL VALOR DESEJA SACAR ?");

    double valorDigitado = 0;
    try {
      valorDigitado = input.nextDouble();
    } catch (InputMismatchException exception) {
      System.out.println("Tipo de dado inválido!");
      exception.printStackTrace();
    }
    if (valorDigitado > conta.getSaldo()) {
      System.out.println("Valor digitado ultrapassa o saldo atual");
      System.out.println("Digite novo valor de saque:");

      try {
        valorDigitado = input.nextDouble();
      } catch (InputMismatchException exception) {
        System.out.println("Tipo de dado inválido!");
        exception.printStackTrace();
      }

      if (valorDigitado > conta.getSaldo()) {
        System.out.println("Valor digitado ultrapassa o saldo atual");
        System.out.println("OPERAÇÃO ENCERRADA!");
        System.exit(0);
      }
    }
    if (valorDigitado == 0) {
      System.out.println("Valor inválido !");
      System.out.println("Digite um valor maior que zero");
      try {
        valorDigitado = input.nextDouble();
      } catch (InputMismatchException exception) {
        System.out.println("Tipo de dado inválido!");
        exception.printStackTrace();
      }
      if (valorDigitado == 0) {
        System.out.println("Valor inválido !");
        System.out.println("OPERAÇÃO ENCERRADA!");
        System.exit(0);
      }
      if (valorDigitado > conta.getSaldo()) {
        System.out.println("Valor inválido !");
        System.out.println("Saldo insuficiente.");
        System.exit(0);

      }
    }
    conta.setSaldo(conta.getSaldo() - valorDigitado);
    // Verifica se o saque foi realizado em conta corrente e cobra a taxa
    // correspondente
    System.out.println("--------------------------------");
    System.out.println("Saque realizado com sucesso !");
    System.out.println("Valor do saque: " + realBrasileiro.format(valorDigitado));
    double taxaSaque = 6.50;
    if (conta instanceof ContaCorrente) {
      if (conta.getSaldo() >= taxaSaque) {
        conta.setSaldo(conta.getSaldo() - taxaSaque);
        System.out.println("Taxa saque conta corrente: " + realBrasileiro.format(taxaSaque));
      }
    }
    System.out.println("Saldo atual: " + realBrasileiro.format(conta.getSaldo()));
    // Chamada ao método 'extratoSaque' da clase 'ExtratoBancario'
    if (conta instanceof ContaPoupanca) {
      try {
        ExtratoBancario.extratoSaque(conta, valorDigitado, taxaSaque, true, false);
      } catch (IOException e) {
        System.out.println("Ocorreu um erro inesperado.");
        e.printStackTrace();
      }
    }
    if (conta instanceof ContaCorrente) {
      try {
        ExtratoBancario.extratoSaque(conta, valorDigitado, taxaSaque, false, true);
      } catch (IOException e) {
        System.out.println("Ocorreu um erro inesperado.");
        e.printStackTrace();
      }
    }

    System.out.println("--------------------------------");
    System.out.print("Voltar ao início ? Sim-1, Não-2");
    int option = 0;
    try {
      option = input.nextInt();
    } catch (InputMismatchException e) {
      System.out.println("Tipo de dado inválido!");
      e.printStackTrace();
    }
    if (option == 1) {
      Menu.main(args);
    } else {
      System.exit(0);
    }

  }

  /**
   * Método utilizado para realizar depósitos em conta (Poupança ou Corrente)
   * <p>
   * O valor do depósito é informado pelo usuário e adicionado da conta do mesmo
   * 
   * @param conta a conta do usuário (Corrente ou Poupança)
   * 
   * @param args  utilizamos este parâmeto para chamar o método main do programa,
   *              pois
   *              neste método está o 'menu principal'
   */
  protected static void depositar(Conta conta, String[] args) {

    System.out.println("--------------------------------");
    System.out.println("SEU SALDO ATUAL: " + realBrasileiro.format(conta.getSaldo()));
    System.out.println("--------------------------------");
    System.out.println("");
    System.out.println("QUAL VALOR DESEJA DEPOSITAR ?");

    double valorDigitado = 0;
    try {
      valorDigitado = input.nextDouble();
    } catch (InputMismatchException exception) {
      System.out.println("Tipo de dado inválido!");
      exception.printStackTrace();
    }

    conta.setSaldo(conta.getSaldo() + valorDigitado);

    System.out.println("--------------------------------");
    System.out.println("Depósito realizado com sucesso !");
    System.out.println("Valor do depósito: " + realBrasileiro.format(valorDigitado));
    // Verifica se a conta é Poupança ou Corrente e cobra as taxas correspondentes
    double valorBonus = 0;
    if (conta instanceof ContaPoupanca) {
      valorBonus = 0.01 * valorDigitado;
      conta.setSaldo(conta.getSaldo() + valorBonus);
      System.out.println("Valor bônus adicionado: " + realBrasileiro.format(valorBonus));
    }
    System.out.println("Saldo atual: " + realBrasileiro.format(conta.getSaldo()));

    // Chamada ao método 'extratoDeposito' da clase 'ExtratoBancario'
    if (conta instanceof ContaPoupanca) {
      try {
        ExtratoBancario.extratoDeposito(conta, valorDigitado, valorBonus, true, false);
      } catch (IOException e) {
        System.out.println("Ocorreu um erro inesperado.");
        e.printStackTrace();
      }
    }
    if (conta instanceof ContaCorrente) {
      try {
        ExtratoBancario.extratoDeposito(conta, valorDigitado, valorBonus, true, false);
      } catch (IOException e) {
        System.out.println("Ocorreu um erro inesperado.");
        e.printStackTrace();
      }
    }

    System.out.println("--------------------------------");
    System.out.println("Voltar ao início ? 1-Sim, 2-Não");
    int option = 0;
    try {
      option = input.nextInt();
    } catch (InputMismatchException exception) {
      System.out.println("Tipo de dado inválido!");
      exception.printStackTrace();
    }
    if (option == 1) {
      Menu.main(args);
    } else {
      System.exit(0);
    }

  }

  /**
   * Método utilizado para realizar o acesso à conta do usuário (Poupança ou
   * Corrente).
   * <p>
   * Solicita informações como número da conta e senha, para poder realizar o
   * acesso à conta.
   * <p>
   * Permite ao usuário realizar saques e depósitos em conta.
   * 
   * @param conta a conta do usuário (Corrente ou Poupança)
   * 
   * @param args  utilizamos este parâmeto para chamar o método main do programa,
   *              pois
   *              neste método está o 'menu principal'
   */
  protected static void acessarConta(ArrayList<Conta> listaContas, String[] args) {

    int numDigitado = 0;
    int senhaDigitada = 0;

    do {
      try {
        System.out.println("INFORME O NÚMERO DA CONTA:");
        numDigitado = input.nextInt();
        System.out.println("INFORME A SENHA DE 4 DÍGITOS");
        senhaDigitada = input.nextInt();
      } catch (InputMismatchException e) {
        System.out.println("Tipo de dado inválido!");
        e.printStackTrace();
      }
    } while (Validations.validarContaeSenhaDeAcesso(listaContas, numDigitado, senhaDigitada) == false);

    // verifica se a conta já existe percorrendo a 'listaContas'
    for (int i = 0; i < listaContas.size(); i++) {
      // Se o número de conta existe
      if (listaContas.get(i).getNumConta() == numDigitado) {
        // Se a senha corresponde ao número da conta
        if (listaContas.get(i).getSenhaConta() == senhaDigitada) {
          // Chamada do método da classe 'Conta'
          Conta.imprimirDetalhesConta(listaContas.get(i));

          System.out.println("SELECIONE UMA OPÇÃO:");
          System.out.println("(1) - SACAR");
          System.out.println("(2) - DEPOSITAR");
          System.out.println("(3) - TRANSFERIR");

          int option = 0;

          try {
            option = input.nextInt();
          } catch (InputMismatchException e) {
            System.out.println("Tipo de dado inválido!");
            e.printStackTrace();
          }

          if (option == 1) {
            Conta.sacar(listaContas.get(i), args);
          }
          if (option == 2) {
            Conta.depositar(listaContas.get(i), args);
          } else {
            Conta.transferir(listaContas.get(i), listaContas, args);
          }

        }
      }
    }
  }

  /**
   * Método utilizado para imprimir os detalhes da conta do usuário
   * <p>
   * Imprime o tipo (Poupança ou Corrente), dados da conta (número, agência,
   * saldo)
   * e os respectivos dados do titular (nome, cpf).
   * 
   * @param conta a conta do usuário (Corrente ou Poupança)
   * 
   * @param args  utilizamos este parâmeto para chamar o método main do programa,
   *              pois
   *              neste método está o 'menu principal'
   */
  protected static void imprimirDetalhesConta(Conta conta) {

    System.out.println("-----DADOS DA CONTA-----");
    System.out.println("TITULAR: " + conta.getTitular());
    if (conta instanceof ContaPoupanca) {
      System.out.println("TIPO DE CONTA: Poupança");
    } else {
      System.out.println("TIPO DE CONTA: Corrente");
    }
    // formatamos a impressão do cpf
    char[] cpfFormatado = (conta.getCpf().toCharArray());
    System.out.println("CPF: " + cpfFormatado[0] + cpfFormatado[1] + cpfFormatado[2] + "." + cpfFormatado[3]
        + cpfFormatado[4] + cpfFormatado[5] + "." + cpfFormatado[6] + cpfFormatado[7] + cpfFormatado[8] + "-"
        + cpfFormatado[9] + cpfFormatado[10]);
    System.out.println("AGÊNCIA: " + conta.getAgencia());
    System.out.println("CONTA: " + conta.getNumConta());
    System.out.println("SALDO: " + realBrasileiro.format(conta.getSaldo()));
    System.out.println("------------------------");
  }

  /**
   * Método utilizado para abertura de conta (Poupança ou Corrente)
   * <p>
   * Possui um cadastro para abertura de conta, onde o usuário deve fornecer
   * informações
   * como por exemplo: o tipo de conta que deseja abrir (Poupança ou Corrente) e
   * suas informações
   * pessoais (nome, cpf).
   * <p>
   * Também é possível obter detalhes sobre os diferentes tipos de conta e suas
   * respectivas taxas.
   * 
   * @param conta a conta do usuário (Corrente ou Poupança)
   * 
   * @param args  utilizamos este parâmeto para chamar o método main do programa,
   *              pois
   *              neste método está o 'menu principal'
   */
  protected static void abrirConta(ArrayList<Conta> listaContas, String[] args) {

    System.out.println("QUAL O TIPO DE CONTA DESEJA ABRIR ?");
    System.out.println("(1) - POUPANÇA");
    System.out.println("(2) - CORRENTE");
    System.out.println("(3) - VERIFICAR DIFERENÇAS ENTRE OS TIPOS");

    int option = 0;

    try {
      option = input.nextInt();
    } catch (InputMismatchException exception) {
      System.out.println("Tipo de dado inválido!");
      exception.printStackTrace();
    }

    switch (option) {

      // CASO 'POUPANÇA' FOR ESCOLHIDA
      case 1:

        input.nextLine();

        try {

          System.out.println("INFORME SEU NOME COMPLETO:");
          String nomeDigitado1 = input.nextLine();
          nomeDigitado1.trim();

          String cpfDigitado1;
          do {
            System.out.println("INFORME SEU CPF");
            cpfDigitado1 = input.nextLine();
          } while (Validations.validarCPF(cpfDigitado1) == false);

          int senhaDigitada1;
          do {
            System.out.println("DIGITE UMA SENHA DE 4 DÍGITOS");
            senhaDigitada1 = input.nextInt();
          } while (Validations.validarSenha(senhaDigitada1) == false);

          int numConta1;
          do {
            numConta1 = aleatory.nextInt((10000 - 1000) + 1) + 1000;
          } while (Validations.validarNumeroDaContaGerado(listaContas, numConta1) == false);

          // Instance of 'ContaPoupanca' object
          ContaPoupanca CP = new ContaPoupanca(nomeDigitado1, cpfDigitado1, "3920-9", numConta1, senhaDigitada1, 0);

          // Add the object 'CP' to the 'listaContas'
          listaContas.add(CP);

          try {
            ExtratoBancario.extratoAberturaConta(CP, true, false);
          } catch (IOException e) {
            e.printStackTrace();
          }

          // Confirma que a conta foi criada com sucesso
          System.out.println();
          System.out.println("Conta criada com sucesso!");
          Conta.imprimirDetalhesConta(CP);

          System.out.println("Voltar ao início ? Sim-1, Não-2");
          option = input.nextInt();
          if (option == 1) {
            Menu.main(args);
          } else {
            System.exit(0);
          }
          break;

        } catch (InputMismatchException exception) {
          System.out.println("Tipo de dado inválido!");
          exception.printStackTrace();
        }

        // CASO 'CORRENTE' FOR ESCOLHIDA
      case 2:

        input.nextLine();

        try {

          System.out.println("INFORME SEU NOME COMPLETO:");
          String nomeDigitado2 = input.nextLine();
          nomeDigitado2.trim();

          String cpfDigitado2;
          do {
            System.out.println("INFORME SEU CPF");
            cpfDigitado2 = input.nextLine();
          } while (Validations.validarCPF(cpfDigitado2) == false);

          int senhaDigitada2;
          do {
            System.out.println("DIGITE UMA SENHA DE 4 DÍGITOS");
            senhaDigitada2 = input.nextInt();
          } while (Validations.validarSenha(senhaDigitada2) == false);

          int numConta2;
          do {
            numConta2 = aleatory.nextInt((10000 - 1000) + 1) + 1000;
          } while (Validations.validarNumeroDaContaGerado(listaContas, numConta2) == false);

          // Instance of 'ContaPoupanca' object
          ContaCorrente CC = new ContaCorrente(nomeDigitado2, cpfDigitado2, "3920-9", numConta2, senhaDigitada2, 0);
          // Add the object 'CP' to the 'listaContas'
          listaContas.add(CC);

          try {
            ExtratoBancario.extratoAberturaConta(CC, false, true);
          } catch (IOException e) {
            e.printStackTrace();
          }

          // Confirma que a conta foi criada com sucesso
          System.out.println();
          System.out.println("Conta criada com sucesso!");
          Conta.imprimirDetalhesConta(CC);

          System.out.println("Voltar ao início ? Sim-1, Não-2");
          option = input.nextInt();
          if (option == 1) {
            Menu.main(args);
          } else {
            System.exit(0);
          }
          break;

        } catch (InputMismatchException exception) {
          System.out.println("Tipo de dado inválido!");
          exception.printStackTrace();
        }

        // CASO 'OPÇÃO 3' FOR ESCOLHIDA
      case 3:

        try {

          System.out.println("------------------------------");
          System.out.println("       CONTA POUPANÇA");
          System.out
              .println("*DEPÓSITO: a cada depósito efetuado é adicionado \n um bônus de 1,0% ao valor depositado");
          System.out.println("*SAQUE: não há taxas para saques realizados");
          System.out.println("------------------------------");
          System.out.println("       CONTA CORRENTE");
          System.out.println("*DEPÓSITO: não há bônus para depósitos");
          System.out.println("*SAQUE: taxa de R$ 6,50 reais para cada depósito realizado");
          System.out.println(
              "Obs: taxa de saque somente será cobrada se o valor do saldo \n após a operação for maior que R$ 6,50");
          System.out.println("------------------------------");

          System.out.println("Voltar ao início ? Sim-1, Não-2");
          option = input.nextInt();
          if (option == 1) {
            Menu.main(args);
          } else {
            System.exit(0);
          }
          break;

        } catch (InputMismatchException exception) {
          System.out.println("Tipo de dado inválido!");
          exception.printStackTrace();
        }

    }

  }

}
