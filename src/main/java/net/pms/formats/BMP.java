package net.pms.formats;

public class BMP extends JPG {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identifier getIdentifier() {
		return Identifier.BMP;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getSupportedExtensions() {
		return new String[] { "bmp", "webp", "wbmp", "pcx" };
	}

	@Override
	public String mimeType() {
		return "image/bmp";
	}
}
