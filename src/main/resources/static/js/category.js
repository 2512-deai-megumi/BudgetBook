/**
 * 家計簿追加、編集画面で収支のタイプにより、動的にカテゴリを変更させる
 */
document.addEventListener("DOMContentLoaded", () => {
	const typeSelect = document.getElementById("typeSelect");
	const categorySelect = document.getElementById("categorySelect");
	
	if (!typeSelect || !categorySelect) {
		return;
	}
	
	const categoryMap = {
		"収入":["給料", "賞与", "副収入", "臨時収入"],
		"支出":["食費", "固定費", "交際費", "日用品", "趣味"]
	};
	
	function updateCategories(selectedType, selectedCategory = ""){
		categorySelect.innerHTML = "";
		
		const defaultOption = document.createElement("option");
		defaultOption.value = "";
		defaultOption.textContent = "選択してください";
		categorySelect.appendChild(defaultOption);
		
		const categories = categoryMap[selectedType] || [];
		
		categories.forEach(category => {
			const option = document.createElement("option");
			option.value = category;
			option.textContent = category;
			
			if (category === selectedCategory){
				option.selected = true;
			}
			
			categorySelect.appendChild(option);
		});
		
		}
		
		typeSelect.addEventListener("change", () => {
			updateCategories(typeSelect.value);
		});
		
		const initialType = typeSelect.value;
		const initialCategory = categorySelect.getAttribute("data-selected");
		
		if(initialType){
			updateCategories(initialType, initialCategory);
		}	
		
});