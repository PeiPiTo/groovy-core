package groovy.swing

import java.awt.BorderLayout
import javax.swing.BorderFactory

/**
 * Demonstrates the use of the Groovy TableModels for viewing tables of any List of objects
 */
class TableDemo {
    
    property frame
    property swing
    
    void run() {
        swing = new SwingBuilder()
        
        model = null
        frame = swing.frame(title:'Groovy TableModel Demo', location:[200,200], size:[300,200]) {
            menuBar {
		        menu(text:'Help') {
		            menuItem() {
		                action(name:'About', closure:{ showAbout() })
		            }
		        }
		    }
		    panel(layout:new BorderLayout()) {
		        model = [['name':'James', 'location':'London'], ['name':'Bob', 'location':'Atlanta'], ['name':'Geir', 'location':'New York']]
		        scrollPane(constraints:BorderLayout.CENTER) {
    	            table() {
    	                tableModel(list:model) {
                            closureColumn(header:'Name', read:{row| return row.name})
                            closureColumn(header:'Location', read:{row| return row.location})
    	                }
    	            }
    	        }
		    }
		}        
		frame.show()
    }
    
    showAbout() {
 		pane = swing.optionPane(message:'This demo shows how GroovySwing can use Groovy closures to create simple table models')
 		dialog = pane.createDialog(frame, 'About GroovySwing')
 		dialog.show()
    }
}
