package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "*")
public class RecipeController {

    private final RecipeRepository repository;

    public RecipeController(RecipeRepository repository) {
        this.repository = repository;
    }

    // ✅ GET ALL
    @GetMapping
    public List<Recipe> getAllRecipes() {
        return repository.findAll();
    }

    // ✅ SEARCH
    @GetMapping("/search")
    public List<Recipe> searchByIngredient(@RequestParam String ingredient) {
        return repository.findByIngredientsContaining(ingredient);
    }

    // ✅ CREATE
    @PostMapping
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        return repository.save(recipe);
    }

    // ✅ UPDATE (FULL UPDATE)
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(
            @PathVariable String id,
            @RequestBody Recipe updatedRecipe
    ) {
        Optional<Recipe> existing = repository.findById(id);

        if (existing.isPresent()) {
            Recipe recipe = existing.get();

            recipe.setTitle(updatedRecipe.getTitle());
            recipe.setIngredients(updatedRecipe.getIngredients());
            recipe.setInstructions(updatedRecipe.getInstructions());

            Recipe saved = repository.save(recipe);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ OPTIONAL: PARTIAL UPDATE (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<Recipe> partialUpdateRecipe(
            @PathVariable String id,
            @RequestBody Recipe updatedRecipe
    ) {
        Optional<Recipe> existing = repository.findById(id);

        if (existing.isPresent()) {
            Recipe recipe = existing.get();

            if (updatedRecipe.getTitle() != null)
                recipe.setTitle(updatedRecipe.getTitle());

            if (updatedRecipe.getIngredients() != null)
                recipe.setIngredients(updatedRecipe.getIngredients());

            if (updatedRecipe.getInstructions() != null)
                recipe.setInstructions(updatedRecipe.getInstructions());

            Recipe saved = repository.save(recipe);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ DELETE (FIXED)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable String id) {
        repository.deleteById(id); // ✅ fixed variable name
        return ResponseEntity.noContent().build();
    }
}