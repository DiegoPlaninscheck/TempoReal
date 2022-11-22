import javax.swing.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class Tela extends JFrame implements Runnable {
    private JPanel tela;
    private JLabel valor1;
    private JLabel valor2;
    private JLabel valor3;
    private JLabel valor4;
    private JLabel valor5;

    DatagramSocket clientSocket;
    String servidor = "localhost";
    int porta = 9090;
    InetAddress IPAddress;
    byte[] sendData = new byte[1024], receiveData = new byte[1024];


    public Tela() {
        criarComponentes();
    }

    private void criarComponentes() {
        setContentPane(tela);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
    }

    private void atualizarValor(String comando, DatagramSocket clientSocket, InetAddress IPAddress, JLabel labelValor) {
        try {
            Long horarioAtual = new Date().getTime();
            sendData = comando.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, porta);
            clientSocket.send(sendPacket);

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            clientSocket.receive(receivePacket);
            labelValor.setText(new String(receivePacket.getData()));

            Long horarioMudanca = new Date().getTime();

            gerarArquivo(horarioAtual, (horarioMudanca - horarioAtual));
            System.out.println(horarioMudanca - horarioAtual);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void gerarArquivo(Long horaAtual, Long tempo) {
        File arquivo = new File("C:\\Users\\Aluno\\Documents\\tempoReal.txt");
        try {
            if (!arquivo.exists()) {
                //cria um arquivo (vazio)
                arquivo.createNewFile();
            }
            //caso seja um diretório, é possível listar seus arquivos e diretórios
            File[] arquivos = arquivo.listFiles();
            //escreve no arquivo
            FileWriter fw = new FileWriter(arquivo, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Tempo: " + horaAtual + " " + tempo);
            bw.newLine();
            bw.close();
            fw.close();
            //faz a leitura do arquivo
            FileReader fr = new FileReader(arquivo);
            BufferedReader br = new BufferedReader(fr);
            //enquanto houver mais linhas
            while (br.ready()) {
                //lê a proxima linha
                String linha = br.readLine();
                //faz algo com a linha
                System.out.println(linha);
            }
            br.close();
            fr.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void run() {
        if (!isVisible()) {
            setVisible(true);
            try {
                clientSocket = new DatagramSocket();

                IPAddress = InetAddress.getByName(servidor);

                while (true) {
                    atualizarValor("sta0", clientSocket, IPAddress, valor1);
                    atualizarValor("st-0", clientSocket, IPAddress, valor2);
                    atualizarValor("sti0", clientSocket, IPAddress, valor3);
                    atualizarValor("sno0", clientSocket, IPAddress, valor4);
                    atualizarValor("sh-0", clientSocket, IPAddress, valor5);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "A janela já está aberta");
        }
    }
}
