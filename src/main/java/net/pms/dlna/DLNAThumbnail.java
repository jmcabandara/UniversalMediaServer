/*
 * Universal Media Server, for streaming any media to DLNA
 * compatible renderers based on the http://www.ps3mediaserver.org.
 * Copyright (C) 2012 UMS developers.
 *
 * This program is a free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; version 2
 * of the License only.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package net.pms.dlna;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import com.drew.metadata.Metadata;
import net.pms.util.ImageFormat;
import net.pms.util.ImagesUtil;
import net.pms.util.ImagesUtil.ScaleType;

/**
 * This class is simply a byte array for holding an {@link ImageIO} supported
 * image with some additional metadata restricted to the DLNA image media
 * format profiles {@code JPEG_*} and {@code PNG_*}.
 *
 * @see DLNAThumbnailInputStream
 *
 * @author Nadahar
 */
public class DLNAThumbnail extends DLNAImage {

	/*
	 * Please note: This class is packed and stored in the database. Any changes
	 * to the data structure (fields) will invalidate any instances already
	 * stored, and will require a wipe of all rows with a stored instance.
	 */
	private static final long serialVersionUID = 8014825365045944137L;

	/**
	 * Creates a new {@link DLNAThumbnail} instance.
	 *
	 * @param bytes the source image in either JPEG or PNG format adhering
	 * to the DLNA restrictions for color space and compression.
	 *
	 * @param imageInfo the {@link ImageInfo} to store with this
	 *                  {@link DLNAThumbnail}.
	 * @param copy whether the source image should be copied or shared.
	 */
	public DLNAThumbnail(byte[] bytes, ImageInfo imageInfo, boolean copy) {
		super(bytes, imageInfo, copy);
	}

	/**
	 * Creates a new {@link DLNAThumbnail} instance.
	 *
	 * @param bytes the source image in either JPEG or PNG format adhering
	 * to the DLNA restrictions for color space and compression.
	 *
	 * @param width the width of the source image.
	 * @param height the height of the source image.
	 * @param format the {@link ImageFormat} of the source image.
	 * TODO: Params
	 * @param copy whether the source image should be copied or shared.
	 */
	public DLNAThumbnail(
		byte[] bytes, int width, int height, ImageFormat format,
		ColorModel colorModel,
		Metadata metadata,
		boolean copy) {
		super(bytes, width, height, format, colorModel, metadata, copy);
	}

	//TODO: Javadocs
	public DLNAThumbnail(byte[] bytes, ImageFormat format, BufferedImage bufferedImage, Metadata metadata, boolean copy) {
		super(bytes, format, bufferedImage, metadata, copy);
	}

	/**
	 * Converts an image to a {@link DLNAThumbnail}. Format support is limited
	 * to that of {@link ImageIO}. Output format will be the same as source
	 * if the source is either JPEG or PNG format adhering to the DLNA
	 * restrictions for color space and compression. If not, the image will be
	 * converted to a DLNA compliant JPEG.
	 *
	 * @param sourceByteArray the source image in a supported format.
	 * @return The populated {@link DLNAThumbnail} or {@code null} if the
	 *         source image could not be parsed.
	 * @throws IOException if the operation fails.
	 */
	public static DLNAThumbnail toThumbnail(byte[] sourceByteArray) throws IOException {
		return toThumbnail(sourceByteArray, 0, 0, null, ImageFormat.SOURCE, false);
	}

	/**
	 * Converts an image to a {@link DLNAThumbnail}. Format support is limited
	 * to that of {@link ImageIO}. {@code outputFormat} is limited to JPEG or
	 * PNG format adhering to the DLNA restrictions for color space and
	 * compression. If {@code outputFormat} doesn't qualify, the image will be
	 * converted to a DLNA compliant JPEG.
	 *
	 * @param sourceByteArray the source image in a supported format.
	 * @param width the new width or 0 to disable scaling.
	 * @param height the new height or 0 to disable scaling.
	 * @param scaleType the {@link ScaleType} to use when scaling.
	 * @param outputFormat the {@link ImageFormat} to generate or
	 *                     {@link ImageFormat#SOURCE} to preserve source format.
	 * @param padToSize whether padding should be used if source aspect doesn't
	 *                  match target aspect.
	 * @return The populated {@link DLNAThumbnail} or {@code null} if the
	 *         source image could not be parsed.
	 * @throws IOException if the operation fails.
	 */
	public static DLNAThumbnail toThumbnail(
		byte[] sourceByteArray,
		int width,
		int height,
		ScaleType scaleType,
		ImageFormat outputFormat,
		boolean padToSize) throws IOException {
		return (DLNAThumbnail) ImagesUtil.transcodeImage(
			sourceByteArray,
			width,
			height,
			scaleType,
			outputFormat,
			true,
			true,
			true,
			padToSize);
	}

	/**
	 * Converts an image to a {@link DLNAThumbnail}. Format support is limited
	 * to that of {@link ImageIO}. Output format will be the same as source
	 * if the source is either JPEG or PNG format adhering to the DLNA
	 * restrictions for color space and compression. If not, the image will be
	 * converted to a DLNA compliant JPEG.
	 *
	 * <p><b>
	 * This method consumes and closes {@code inputStream}.
	 * </b>
	 *
	 * @param inputStream the source image in a supported format.
	 * @return The populated {@link DLNAThumbnail} or {@code null} if the
	 *         source image could not be parsed.
	 * @throws IOException if the operation fails.
	 */
	public static DLNAThumbnail toThumbnail(InputStream inputStream) throws IOException {
		return toThumbnail(inputStream, 0, 0, null, ImageFormat.SOURCE, false);
	}

	/**
	 * Converts an image to a {@link DLNAThumbnail}. Format support is limited
	 * to that of {@link ImageIO}. {@code outputFormat} is limited to JPEG or
	 * PNG format adhering to the DLNA restrictions for color space and
	 * compression. If {@code outputFormat} doesn't qualify, the image will be
	 * converted to a DLNA compliant JPEG.
	 *
	 * <p><b>
	 * This method consumes and closes {@code inputStream}.
	 * </b>
	 * @param inputStream the source image in a supported format.
	 * @param width the new width or 0 to disable scaling.
	 * @param height the new height or 0 to disable scaling.
	 * @param scaleType the {@link ScaleType} to use when scaling.
	 * @param outputFormat the {@link ImageFormat} to generate or
	 *                     {@link ImageFormat#SOURCE} to preserve source format.
	 * @param padToSize whether padding should be used if source aspect doesn't
	 *                  match target aspect.
	 * @return The populated {@link DLNAThumbnail} or {@code null} if the
	 *         source image could not be parsed.
	 * @throws IOException if the operation fails.
	 */
	public static DLNAThumbnail toThumbnail(
		InputStream inputStream,
		int width,
		int height,
		ScaleType scaleType,
		ImageFormat outputFormat,
		boolean padToSize
	) throws IOException {
		if (inputStream == null) {
			return null;
		}

		return (DLNAThumbnail) ImagesUtil.transcodeImage(
			inputStream,
			width,
			height,
			scaleType,
			outputFormat,
			true,
			true,
			true,
			padToSize);
	}

	/**
	 * Converts and scales the thumbnail according to the given
	 * {@link DLNAImageProfile}. Preserves aspect ratio. Format support is
	 * limited to that of {@link ImageIO}.
	 *
	 * @param imageProfile the DLNA media profile to adhere to for the output.
	 * TODO: Params
	 * @param padToSize Whether padding should be used if source aspect doesn't
	 *                  match target aspect.
	 * @return The scaled and/or converted thumbnail, {@code null} if the
	 *         source is {@code null}.
	 * @exception IOException if the operation fails.
	 */
	@Override
	public DLNAThumbnail transform(DLNAImageProfile imageProfile, boolean updateMetadata, boolean padToSize) throws IOException {
		switch (imageProfile.toInt()) {
			case DLNAImageProfile.JPEG_LRG_INT:
				return (DLNAThumbnail) ImagesUtil.transcodeImage(
					this.getBytes(false), 4096, 4096, ScaleType.MAX, ImageFormat.JPEG, updateMetadata, true, true, padToSize
				);
			case DLNAImageProfile.JPEG_MED_INT:
				return (DLNAThumbnail) ImagesUtil.transcodeImage(
					this.getBytes(false), 1024, 768, ScaleType.MAX, ImageFormat.JPEG, updateMetadata, true, true, padToSize
				);
			case DLNAImageProfile.JPEG_RES_H_V_INT:
				return (DLNAThumbnail) ImagesUtil.transcodeImage(
					this.getBytes(false), imageProfile.getH(), imageProfile.getV(), ScaleType.EXACT, ImageFormat.JPEG, updateMetadata, true, true, padToSize
				);
			case DLNAImageProfile.JPEG_SM_INT:
				return (DLNAThumbnail) ImagesUtil.transcodeImage(
					this.getBytes(false), 640, 480, ScaleType.MAX, ImageFormat.JPEG, updateMetadata, true, true, padToSize
				);
			case DLNAImageProfile.JPEG_TN_INT:
				return (DLNAThumbnail) ImagesUtil.transcodeImage(
					this.getBytes(false), 160, 160, ScaleType.MAX, ImageFormat.JPEG, updateMetadata, true, true, padToSize
				);
			case DLNAImageProfile.PNG_LRG_INT:
				return (DLNAThumbnail) ImagesUtil.transcodeImage(
					this.getBytes(false), 4096, 4096, ScaleType.MAX, ImageFormat.PNG, updateMetadata, true, true, padToSize
				);
			case DLNAImageProfile.PNG_TN_INT:
				return (DLNAThumbnail) ImagesUtil.transcodeImage(
					this.getBytes(false), 160, 160, ScaleType.MAX, ImageFormat.PNG, updateMetadata, true, true, padToSize
				);
			default:
				return (DLNAThumbnail) ImagesUtil.transcodeImage(
					this.getBytes(false), 160, 160, ScaleType.MAX, ImageFormat.JPEG, updateMetadata, true, true, padToSize
				);
		}
	}

	@Override
	public DLNAThumbnail copy() {
		return new DLNAThumbnail(bytes, imageInfo, true);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(80);
		sb.append("DLNAThumbnail: Format = ").append(imageInfo.getFormat())
		.append(", Width = ").append(imageInfo.getWidth())
		.append(", Height = ").append(imageInfo.getHeight())
		.append(", Size = ").append(bytes != null ? bytes.length : 0);
		return sb.toString();
	}
}
