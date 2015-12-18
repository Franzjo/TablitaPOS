package tablita;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.File;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.mortbay.jetty.MimeTypes;
import tablita.Dtos.VentasDTO;
import tablita.persistencia.Ventas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Creado por akino on 12-15-15.
 */
public class GeneradorJson {

    private static String FILE_NAME = "db.json";

    public static void generarJson(List<Ventas> ventas){

        ObjectMapper mapper = new ObjectMapper();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<VentasDTO> ventasDTOs = new ArrayList<>();

        for (Ventas v : ventas) {
            VentasDTO vdto = modelMapper.map(v, VentasDTO.class);
            ventasDTOs.add(vdto);
        }
        
        try {

            File body = new File();
            body.setTitle(FILE_NAME);
            body.setDescription("datos sobre ventas");
            body.setMimeType(MimeTypes.TEXT_JSON);

            java.io.File fileContent = new java.io.File(FILE_NAME);

            mapper.writeValue(fileContent, ventasDTOs);

            FileContent mediaContent = new FileContent(MimeTypes.TEXT_JSON, fileContent);

            DriveServices.upload(body,mediaContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
