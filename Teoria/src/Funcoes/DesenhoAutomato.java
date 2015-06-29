package Funcoes;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;


public class DesenhoAutomato extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1821385769531784699L;
	private Automato afd;
	private mxGraph graph;
	private mxGraphComponent graphComponent;
	private Object parent;

	public DesenhoAutomato(Automato afd, String titulo) {
		super(titulo);
		this.setSize(60 * afd.getEstados().size() + 120, 60 * afd.getEstados().size() + 100);
		this.setResizable(false);
        this.setAlwaysOnTop(true);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((screenSize.width-this.getWidth())/2, (screenSize.height-this.getHeight())/2, this.getWidth(), this.getHeight());
        
		this.afd = afd;
		
		this.atualizar();
	}

	public Automato getAfd() {
		return afd;
	}

	public void setAfd(Automato afd) {
		this.afd = afd;
	}
	
	
	public void atualizar() {
		if (graphComponent != null) {
            this.getContentPane().remove(graphComponent);
        }
		
		graph = new mxGraph();
		parent = graph.getDefaultParent();

		this.desenharAFD();
		
		graphComponent = new mxGraphComponent(graph);
		
		graphComponent.setEnabled(false);
		graphComponent.setEventsEnabled(true);
		graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
    
            }
        });
		
		getContentPane().add(graphComponent);
	}
	
	private void desenharAFD() {
		int tam = 0;
		double raio = 0, x = 0, y = 0, angulo = 0;
		List<String> estados = afd.getEstados();
		String estado = "";
        ArrayList<String> destinos = new ArrayList<String>();
		List<String> simbolos = afd.getSimbolos();
		String estadoInicial = afd.getEstadoInicial();
		List<String> estadosFinais = afd.getEstadosFinais();
		
		List<Object> vertices = new ArrayList<Object>();
		
		graph.getModel().beginUpdate();
		
		try
		{
			tam = estados.size();
			angulo = 2 * Math.PI / tam;
			raio = 30 * tam;
			for (int i = 0; i < tam; i++) {
				estado = estados.get(i);
				x = Math.cos(Math.PI+angulo*i)*raio+raio+25;
				y = Math.sin(Math.PI+angulo*i)*raio+raio;
				if(estado.equals(estadoInicial)) {
					if(estadosFinais.contains(estado)) {
						Object o = graph.insertVertex(parent, null, "", x-12, y-15, 0,0, null), v;
						v = graph.insertVertex(parent, null, estado, x, y, 50,50,"ROUNDED;fillColor=#fcfeff;shape=ellipse;fontColor=#000000;fontStyle=1;strokeColor=black;strokeWidth=4");
						vertices.add(v);
						graph.insertEdge(parent, null, "", o, v, "fontColor=#000000;fontStyle=1;strokeColor=black");
					} else {
						Object o = graph.insertVertex(parent, null, "", x-12, y-15, 0,0, null), v;
						v = graph.insertVertex(parent, null, estado, x, y, 50,50,"ROUNDED;fillColor=#fcfeff;shape=ellipse;fontColor=#000000;fontStyle=1;strokeColor=black");
						vertices.add(v);
						graph.insertEdge(parent, null, "", o, v, "fontColor=#000000;fontStyle=1;strokeColor=black");
					}
				} else if(estadosFinais.contains(estado)) {
					vertices.add(graph.insertVertex(parent, null, estado, x, y, 50,50,"ROUNDED;fillColor=#fcfeff;shape=ellipse;fontColor=#000000;fontStyle=1;strokeColor=black;strokeWidth=4"));
				} else {
					vertices.add(graph.insertVertex(parent, null, estado, x, y, 50,50,"ROUNDED;fillColor=#fcfeff;shape=ellipse;fontColor=#000000;fontStyle=1;strokeColor=black"));
				}
			}
			for (int i = 0; i < tam; i++) {
				estado = estados.get(i);
				for (String simbolo : simbolos) {
					//destinos = afd.funcaoPrograma(estado, simbolo);
                                        destinos = afd.funcaoProgramaAFN(estado, simbolo);
                                        for (String destino : destinos) {
                                            graph.insertEdge(parent, null, simbolo, vertices.get(i), vertices.get(estados.indexOf(destino)), "fontColor=#000000;fontStyle=1;strokeColor=black");
                                        }
				}
			}
		}
		finally
		{
			graph.getModel().endUpdate();
		}

		mxParallelEdgeLayout p = new mxParallelEdgeLayout(graph, 30);
		p.execute(parent);
	}

        
}
