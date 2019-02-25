package com.hendisantika.springbootdropdownexample;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.yaml.snakeyaml.Yaml;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-dropdown-example
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-02-26
 * Time: 06:26
 */
@Controller
public class DropdownController {

    private Map<String, LinkedHashMap<String, String>> dropdownMap;

    public DropdownController(@Value("classpath:dropdownValues.yml") Resource dropdownYaml) {
        InputStream input = null;
        try {
            input = dropdownYaml.getInputStream();
            Yaml yaml = new Yaml();
            dropdownMap = yaml.load(input);
            System.out.println("Dropdown Map: " + dropdownMap);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping("/")
    public String displayExample(ShapeForm shapeForm, Model model) {

        return "index";
    }

    @RequestMapping(value = "/submitShape", params = {"save"}, method = RequestMethod.POST)
    public String saveItem(@Valid ShapeForm shapeForm, BindingResult bindingResult,
                           Model model) {
        System.out.println(shapeForm.toString());
        return "redirect:/";
    }

    @ModelAttribute("shapeTypes")
    public Map<String, String> getShapeTypes() {
        Map<String, String> shapeTypeMap = dropdownMap.keySet()
                .stream()
                .distinct()
                .sorted()
                .collect(Collectors.toMap(Function.identity(), Function.identity(),
                        (u, v) -> {
                            throw new IllegalStateException(String.format("Duplicate key %s", u));
                        },
                        LinkedHashMap::new));
        return shapeTypeMap;
    }

    @ModelAttribute("shapeSuggestionsJSON")
    public String getShapeSuggestionsJSON() {
        String shapeSuggestions = "";
        Gson gson = new Gson();
        shapeSuggestions = gson.toJson(dropdownMap);
        System.out.println("Shape Suggestions: " + shapeSuggestions);
        return shapeSuggestions;
    }
}

