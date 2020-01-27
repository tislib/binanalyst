import {AfterContentInit, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {Calculator} from "../model/calculator";
import {BitData} from "../model/bit-data";

declare var mxClient: any;
declare var mxUtils: any;
declare var mxRubberband: any;
declare var mxGraph: any;
declare var mxConstants: any;
declare var mxPerimeter: any;
declare var mxEdgeStyle: any;
declare var mxHierarchicalLayout: any;

@Component({
  selector: 'app-visual-graph-calculator',
  template: '',
  styleUrls: ['./visual-graph-calculator.component.scss']
})
export class VisualGraphCalculatorComponent {

  private _data: Calculator;

  @Input()
  set data(value: Calculator) {
    this._data = value;
    this.render();
  }

  get data(): Calculator {
    return this._data;
  }

  constructor(private container: ElementRef) {
  }

  render() {
    this.container.nativeElement.innerHTML = "";
    // Creates the graph inside the given container
    var graph = new mxGraph(this.container.nativeElement);

    // Enables rubberband selection
    // new mxRubberband(graph);

    // Gets the default parent for inserting new cells. This
    // is normally the first child of the root (ie. layer 0).
    var parent = graph.getDefaultParent();

    graph.setConnectable(false);

    // Adds cells to the model in a single step
    graph.getModel().beginUpdate();
    try {
      const bitVertexMap: Map<String, any> = new Map<String, any>();

      this.data.input.forEach(bitData => {
        const vertex = this.createBitDataVertex(graph, parent, bitData, bitData.name);
        vertex.setStyle('shape=ellipse;fillColor=#EFFF88');
        bitVertexMap.set(bitData.name, vertex);
      });

      this.data.middle.forEach(bitData => {
        let label = '';
        if (bitData.type == 'operational') {
          label = bitData.name + '(' + bitData.operation + ')';
          if (bitData.operation == 'NOT') {
            label = '!' + bitData.bits[0];
          }
        } else {
          label = bitData.name;
        }

        const vertex = this.createBitDataVertex(graph, parent, bitData, label);
        bitVertexMap.set(bitData.name, vertex);
      });

      this.data.output.forEach(bitData => {
        if (bitData.type == 'operational') {
          const vertex = bitVertexMap.get(bitData.name);
          vertex.setStyle('shape=ellipse;fillColor=#88FF88');
        }
      });

      this.data.middle.forEach(bitData => {
        if (bitData.type == 'operational') {

          bitData.bits.forEach(otherBit => {
            graph.insertEdge(parent, null, '', bitVertexMap.get(otherBit), bitVertexMap.get(bitData.name));
          });
        }
      });

      new mxHierarchicalLayout(graph).execute(graph.getDefaultParent());


      // var v1 = graph.insertVertex(parent, null, 'Hello,', 20, 20, 80, 30);
      // var v2 = graph.insertVertex(parent, null, 'World!', 200, 150, 80, 30);
      // var e1 = graph.insertEdge(parent, null, '', v1, v2);
    } finally {
      // Updates the display
      graph.getModel().endUpdate();
    }
  }

  private createBitDataVertex(graph: any, parent: any, bitData: BitData, label: string) {
    return graph.insertVertex(parent, null, label, 20, 20, 80, 30);
  }
}
