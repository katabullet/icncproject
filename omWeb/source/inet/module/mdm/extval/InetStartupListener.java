package inet.module.mdm.extval;

import org.apache.myfaces.extensions.validator.core.ExtValContext;
import org.apache.myfaces.extensions.validator.core.renderkit.ExtValRendererProxy;
import org.apache.myfaces.extensions.validator.core.startup.AbstractStartupListener;

/**
 *
 * Quirk to enbale extVal on icefaces, details can be found here :
 *
 * http://os890.blogspot.com/search/label/icefaces
 *
 * @author mme80
 *
 */
public class InetStartupListener extends AbstractStartupListener {


	private static final long serialVersionUID = 1L;

	@Override
	protected void init() {
		ExtValContext.getContext().addGlobalProperty(ExtValRendererProxy.KEY, null);
	}

}
