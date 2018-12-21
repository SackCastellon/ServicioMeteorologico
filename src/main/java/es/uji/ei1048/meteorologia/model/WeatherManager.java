package es.uji.ei1048.meteorologia.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.github.soc.directories.ProjectDirectories;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Locale;

public class WeatherManager {

    private static final Logger logger = LogManager.getLogger(WeatherManager.class);

    private static final int MAX_DATA_PER_FILE = 7;

    private static final @NotNull ProjectDirectories dirs = ProjectDirectories.from(null, "UJI", "ServicioMeteorologico"); //NON-NLS

    private static final @NotNull WeatherManager INSTANCE = new WeatherManager();

    private WeatherManager() {
    }

    public static @NotNull WeatherManager getInstance() {
        return INSTANCE;
    }

    public boolean save(final @NotNull WeatherData data) {
        try {
            final @NotNull Path folder = Paths.get(dirs.dataDir)
                    .resolve(data.getCity().getCountry().toLowerCase(Locale.ENGLISH).trim())
                    .resolve(data.getCity().getName().toLowerCase(Locale.ENGLISH).trim());

            if (Files.notExists(folder))
                Files.createDirectories(folder);

            final @NotNull Path file = folder.resolve("data.json"); //NON-NLS

            final @NotNull JsonArray json;
            try (final BufferedReader in = Files.newBufferedReader(file)) {
                json = new JsonParser().parse(in).getAsJsonArray();
            }

            if (json.size() >= MAX_DATA_PER_FILE)
                throw new MaxFileDataExcededException();

            final @NotNull Gson gson = new Gson();

            json.add(gson.toJsonTree(data, WeatherData.class));

            try (final BufferedWriter out = Files.newBufferedWriter(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                gson.toJson(json, out);
            }
        } catch (final IOException e) {
            logger.error("Failed to save weather data", e); //NON-NLS
            return false;
        }
        return true;
    }

    public @NotNull SaveFile load(final @NotNull String filename) {
        try {
            final @NotNull List<WeatherData> data;
            try (final BufferedReader in = Files.newBufferedReader(Paths.get(filename))) {
                data = new Gson().fromJson(in, new TypeToken<List<WeatherData>>() {
                }.getType());
            }

            return null; // TODO
        } catch (final IOException e) {
            logger.error("Failed to load weather data", e); //NON-NLS
        }
        return null; // TODO
    }
}
